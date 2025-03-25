package com.example.hospitalmanagement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class PatientDashboardActivity extends AppCompatActivity {
    private static final int REQUEST_CALL_PHONE = 1;
    private RecyclerView doctorRecyclerView;
    private Button bookAppointmentButton, logoutBtn, doctorDetailsButton;
    private FirebaseFirestore db;
    private ScrollView doctorDetailsScrollView;
    private TextView doctorDetailsText;
    private FirebaseAuth mAuth;
    private List<Doctor> doctorList;
    private DoctorAdapter doctorAdapter;
    private Doctor selectedDoctor;
    TextView selectedDoctorTextView;

    private Button viewStatusButton;
    private TextView statusTextView;
    private boolean isStatusVisible = false;
    private Button updateStatusButton;
    private TextView statusTextView1;
    private boolean isStatusVisible1 = false;
    private Button fetchApprovedStatusButton;

    private ImageView homeButton;
    private CardView homeOptionsCard;
    private Button callAmbulanceButton, helplineButton, aboutHospitalButton;
    private static final String PATIENT_UID = "au3NyjugG2Z6pJBUN1m9sn5uNo02";
    // Assistant's UID

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        ScrollView doctorDetailsScrollView = findViewById(R.id.doctorDetailsScrollView);
        TextView doctorDetailsText = findViewById(R.id.doctorDetailsText);
        Button doctorDetailsButton = findViewById(R.id.doctorDetailsButton);
        Button closeDoctorDetailsButton = findViewById(R.id.closeDoctorDetailsButton);
        selectedDoctorTextView = findViewById(R.id.selectedDoctorTextView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Initialize Doctor Details Button and Views

        Button viewPrescriptionButton = findViewById(R.id.viewPrescriptionButton);
        viewPrescriptionButton.setOnClickListener(v -> loadPrescriptions());
        // Initialize the views
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);
        logoutBtn = findViewById(R.id.logoutBtn);


        // Initialize views
        homeButton = findViewById(R.id.homeButton);
        homeOptionsCard = findViewById(R.id.homeOptionsCard);
        callAmbulanceButton = findViewById(R.id.callAmbulanceButton);
        helplineButton = findViewById(R.id.helplineButton);
        aboutHospitalButton = findViewById(R.id.aboutHospitalButton);

        // Set click listener for Home Icon
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHomeOptions();
            }
        });

        // Set click listener for Call Ambulance Button
        callAmbulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("01310388215");
            }
        });

        // Set click listener for Helpline Button
        helplineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelplineDialog();
            }
        });

        // Set click listener for About Hospital Button
        aboutHospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutHospitalDialog();
            }
        });


        updateStatusButton = findViewById(R.id.updateStatusButton);
        statusTextView1 = findViewById(R.id.statusTextView1);

        // Set click listener for the Update button
        updateStatusButton.setOnClickListener(v -> fetchAssistantStatus());
        // Initialize views
        viewStatusButton = findViewById(R.id.viewStatusButton);
        statusTextView = findViewById(R.id.statusTextView);

        viewStatusButton.setOnClickListener(v -> fetchStatus());

        // Set up RecyclerView for doctors
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorList = new ArrayList<>();

        doctorAdapter = new DoctorAdapter(doctorList, doctor -> {
            selectedDoctor = doctor;
            selectedDoctorTextView.setVisibility(View.VISIBLE); // Show the TextView
            selectedDoctorTextView.setText("Selected Doctor: " + doctor.getUsername());
            Toast.makeText(PatientDashboardActivity.this, "Selected Doctor: " + doctor.getUsername(), Toast.LENGTH_SHORT).show();
        });

        doctorRecyclerView.setAdapter(doctorAdapter);

        // Initially hide the RecyclerView
        doctorRecyclerView.setVisibility(View.GONE);

        // Book appointment button click event
        bookAppointmentButton.setOnClickListener(v -> {
            if (doctorRecyclerView.getVisibility() == View.GONE) {
                // Show doctors list
                doctorRecyclerView.setVisibility(View.VISIBLE);
                loadDoctors();
            } else {

                if (selectedDoctor == null) {
                    Toast.makeText(PatientDashboardActivity.this, "Please select a doctor", Toast.LENGTH_SHORT).show();
                } else {
                    showBookingDialog();
                }
            }
        });




        doctorDetailsButton.setOnClickListener(v -> {

                String doctorDetails = "\n\n\n1. Dr. Ayesha Rahman\nSpecialization: Cardiologist\nContact: +880 1712 345678\nEmail: ayesha.rahman@hospital.com\nAssistant: Mehedi Hasan\nAssistant Contact: +880 1712 987654\nAssistant Email: mehedi.hasan@hospital.com\n\n"
                        + "2. Dr. Tanvir Ahmed\nSpecialization: Neurologist\nContact: +880 1813 456789\nEmail: tanvir.ahmed@hospital.com\nAssistant: Nusrat Jahan\nAssistant Contact: +880 1813 987654\nAssistant Email: nusrat.jahan@hospital.com\n\n"
                        + "3. Dr. Rashedul Karim\nSpecialization: Orthopedic Surgeon\nContact: +880 1914 567890\nEmail: rashedul.karim@hospital.com\nAssistant: Fahim Alam\nAssistant Contact: +880 1914 987654\nAssistant Email: fahim.alam@hospital.com\n\n"
                        + "4. Dr. Sumaiya Haque\nSpecialization: Pediatrician\nContact: +880 1555 678901\nEmail: sumaiya.haque@hospital.com\nAssistant: Tanjina Akter\nAssistant Contact: +880 1555 987654\nAssistant Email: tanjina.akter@hospital.com\n\n"
                        + "5. Dr. Imran Hossain\nSpecialization: Dermatologist\nContact: +880 1623 789012\nEmail: imran.hossain@hospital.com\nAssistant: Arafat Islam\nAssistant Contact: +880 1623 987654\nAssistant Email: arafat.islam@hospital.com\n\n"
                        + "6. Dr. sajib\nSpecialization: Dermatologist\nContact: +880 1623 789012\nEmail: imran.hossain@hospital.com\nAssistant: md abul kalam azad\nAssistant Contact: +880 1712367557\nAssistant Email: rajbarienterprise123@gmail.com";;

                doctorDetailsText.setText(doctorDetails);
                doctorDetailsScrollView.setVisibility(View.VISIBLE);
            });

            closeDoctorDetailsButton.setOnClickListener(v -> {
                doctorDetailsScrollView.setVisibility(View.GONE);
            });



        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(PatientDashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
        // Logout button click event
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(PatientDashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    private void toggleHomeOptions() {
        if (homeOptionsCard.getVisibility() == View.GONE) {
            homeOptionsCard.setVisibility(View.VISIBLE); // Show the card
        } else {
            homeOptionsCard.setVisibility(View.GONE); // Hide the card
        }
    }

    // Load doctors from Firestore
    private void loadDoctors() {
        db.collection("doctors").addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (error != null) {
                Toast.makeText(PatientDashboardActivity.this, "Error loading doctors", Toast.LENGTH_SHORT).show();
                return;
            }
            if (queryDocumentSnapshots != null) {
                doctorList.clear();
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    Doctor doctor = dc.getDocument().toObject(Doctor.class);
                    doctor.setUid(dc.getDocument().getId());
                    doctorList.add(doctor);
                }
                doctorAdapter.notifyDataSetChanged();
            }
        });
    }

    // Show the appointment booking dialog
    private void showBookingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_book_appointment, null);
        builder.setView(view);

        Spinner slotSpinner = view.findViewById(R.id.slotSpinner);
        Button selectDateButton = view.findViewById(R.id.selectDateButton);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        final String[] selectedDate = {""};

        // Populate available slots
        List<String> availableSlots = selectedDoctor.getAvailableSlots();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableSlots);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slotSpinner.setAdapter(spinnerAdapter);

        // Date selection
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                selectedDate[0] = year + "-" + (month + 1) + "-" + dayOfMonth;
                selectDateButton.setText(selectedDate[0]);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Confirm the appointment booking
        confirmButton.setOnClickListener(v -> {
            String slot = slotSpinner.getSelectedItem().toString();
            if (selectedDate[0].isEmpty() || slot.equals("No slots available")) {
                Toast.makeText(PatientDashboardActivity.this, "Please select a valid date and time slot", Toast.LENGTH_SHORT).show();
                return;
            }
            bookAppointment(selectedDoctor.getUid(), selectedDate[0], slot);
        });

        builder.create().show();
    }

    // Book the appointment and save it to Firestore
    private void bookAppointment(String doctorId, String date, String time) {
        String patientId = mAuth.getCurrentUser().getUid();
        Appointment appointment = new Appointment(patientId, doctorId, "Pending", date, time);
        db.collection("appointments").add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PatientDashboardActivity.this, "Appointment request sent", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PatientDashboardActivity.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadPrescriptions() {
        db.collection("prescriptions")
                .whereEqualTo("patientId", "au3NyjugG2Z6pJBUN1m9sn5uNo02")
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading prescriptions", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            Prescription prescription = dc.getDocument().toObject(Prescription.class);
                            // Display the prescription details to the patient
                            showPrescriptionDetails(prescription);
                        }
                    } else {
                        Toast.makeText(this, "No prescriptions found", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPrescriptionDetails(Prescription prescription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_view_prescription, null);
        builder.setView(view);

        TextView medicineTextView = view.findViewById(R.id.medicineTextView);
        TextView dosageTextView = view.findViewById(R.id.dosageTextView);

        medicineTextView.setText("Medicine: " + prescription.getMedicine());
        dosageTextView.setText("Dosage: " + prescription.getDosage());

        builder.create().show();
    }
    // Add this code in the PatientDashboardActivity class

    // Method to fetch approved status sent by the doctor
    // Method to fetch approved status sent by the doctor
    private void fetchStatus() {
        if (isStatusVisible) {
            // Hide the status
            statusTextView.setVisibility(View.GONE);
            isStatusVisible = false;
        } else {
            // Fetch and show the status
            db.collection("patients")
                    .document(PATIENT_UID)
                    .collection("appointmentStatus")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Get the latest document
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String date = document.getString("date");
                            String time = document.getString("time");
                            String status = document.getString("status");

                            // Display the status in the TextView
                            String statusMessage = "Status: " + status + "\nDate: " + date + "\nTime: " + time;
                            statusTextView.setText(statusMessage);
                            statusTextView.setVisibility(View.VISIBLE); // Show the status
                            isStatusVisible = true; // Update the flag
                        } else {
                            // No status found
                            statusTextView.setText("No status found.");
                            statusTextView.setVisibility(View.VISIBLE); // Show the message
                            isStatusVisible = true; // Update the flag
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                        Toast.makeText(this, "Failed to fetch status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FetchStatus", "Error fetching status", e);
                    });
        }
    }
    // Fetch Assistant's Status
    private void fetchAssistantStatus() {
        if (isStatusVisible1) {
            // Hide the status
            statusTextView1.setVisibility(View.GONE);
            isStatusVisible1 = false;
        } else {
            // Fetch and show the assistant's status
            db.collection("assistantStatus")
                    .document(PATIENT_UID)
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String message = document.getString("message");
                            statusTextView1.setText("Assistant's Status: " + message);
                            statusTextView1.setVisibility(View.VISIBLE);
                            isStatusVisible1 = true;
                        } else {
                            statusTextView1.setText("No assistant status found.");
                            statusTextView1.setVisibility(View.VISIBLE);
                            isStatusVisible1 = true;
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch assistant status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FetchAssistantStatus", "Error fetching assistant status", e);
                    });
        }
    }
    private void makePhoneCall(String phoneNumber) {
        // Check if CALL_PHONE permission is granted
        if (ContextCompat.checkSelfPermission(PatientDashboardActivity.this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(PatientDashboardActivity.this,
                    new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        } else {
            // Permission already granted, make the call
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }
    private void showAboutHospitalDialog() {
        // Create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Our Hospital");

        // Set custom layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_about_hospital, null);
        builder.setView(view);

        // Initialize the Contact Us button
        Button btnContactUs = view.findViewById(R.id.btnContactUs);
        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("01310388215"); // Directly call the contact number
            }
        });

        // Add a button to close the dialog
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showHelplineDialog() {
        // Create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Helpline");

        // Set custom layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_helpline, null);
        builder.setView(view);

        // Initialize the Call Now button
        Button btnCallHelpline = view.findViewById(R.id.btnCallHelpline);
        btnCallHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("01310388215"); // Directly call the helpline number
            }
        });

        // Add a button to close the dialog
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the call
                makePhoneCall("01310388215");
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied. Cannot make the call.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}






