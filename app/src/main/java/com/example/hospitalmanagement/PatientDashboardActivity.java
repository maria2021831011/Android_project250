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
    private CardView doctorDetailsCardView;
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
    private Button viewPrescriptionButton;

    private static final String PATIENT_UID = "au3NyjugG2Z6pJBUN1m9sn5uNo02";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        doctorDetailsCardView = findViewById(R.id.doctorDetailsScrollView);
        doctorDetailsText = findViewById(R.id.doctorDetailsText);
        doctorDetailsButton = findViewById(R.id.doctorDetailsButton);
        Button closeDoctorDetailsButton = findViewById(R.id.closeDoctorDetailsButton);
        selectedDoctorTextView = findViewById(R.id.selectedDoctorTextView);


        viewPrescriptionButton = findViewById(R.id.viewPrescriptionButton);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);
        logoutBtn = findViewById(R.id.logoutBtn);
        viewStatusButton = findViewById(R.id.viewStatusButton);
        updateStatusButton = findViewById(R.id.updateStatusButton);
        statusTextView = findViewById(R.id.statusTextView);
        statusTextView1 = findViewById(R.id.statusTextView1);

        Button callAmbulanceButton = findViewById(R.id.callAmbulanceButton);
        Button helplineButton = findViewById(R.id.helplineButton);
        Button aboutHospitalButton = findViewById(R.id.aboutHospitalButton);

        // Set up recyclerView for doctors
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorList = new ArrayList<>();
// doctor select part
        doctorAdapter = new DoctorAdapter(doctorList, doctor -> {
            selectedDoctor = doctor;
            selectedDoctorTextView.setVisibility(View.VISIBLE);
            selectedDoctorTextView.setText("Selected Doctor: " + doctor.getUsername());
           //shows popup msg
            Toast.makeText(PatientDashboardActivity.this, "Selected Doctor: " + doctor.getUsername(), Toast.LENGTH_SHORT).show();
        });

        doctorRecyclerView.setAdapter(doctorAdapter);
        doctorRecyclerView.setVisibility(View.GONE);

        //book appointment  event
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

        //doctor details button
        doctorDetailsButton.setOnClickListener(v -> {
            String doctorDetails = "\n\n1. Dr. Ayesha Rahman\nSpecialization: Cardiologist\nContact: +880 1712 345678\nEmail: ayesha.rahman@hospital.com\n\n" +
                    "2. Dr. Tanvir Ahmed\nSpecialization: Neurologist\nContact: +880 1813 456789\nEmail: tanvir.ahmed@hospital.com\n\n" +
                    "3. Dr. Rashedul Karim\nSpecialization: Orthopedic Surgeon\nContact: +880 1914 567890\nEmail: rashedul.karim@hospital.com\n\n" +
                    "4. Dr. Sumaiya Haque\nSpecialization: Pediatrician\nContact: +880 1555 678901\nEmail: sumaiya.haque@hospital.com\n\n" +
                    "5. Dr. Imran Hossain\nSpecialization: Dermatologist\nContact: +880 1623 789012\nEmail: imran.hossain@hospital.com\n\n" +
                    "6. Dr. Sajib\nSpecialization: Dermatologist\nContact: +880 1623 789012\nEmail: imran.hossain@hospital.com";

            doctorDetailsText.setText(doctorDetails);
            doctorDetailsCardView.setVisibility(View.VISIBLE);
        });

        closeDoctorDetailsButton.setOnClickListener(v -> {
            doctorDetailsCardView.setVisibility(View.GONE);
        });

        //view prescription button
        viewPrescriptionButton.setOnClickListener(v -> loadPrescriptions());

        //status buttons
        viewStatusButton.setOnClickListener(v -> fetchStatus());
        updateStatusButton.setOnClickListener(v -> fetchAssistantStatus());

        //emergency buttons
        callAmbulanceButton.setOnClickListener(v -> makePhoneCall("01310388215"));
        helplineButton.setOnClickListener(v -> showHelplineDialog());
        aboutHospitalButton.setOnClickListener(v -> showAboutHospitalDialog());

        //logout button
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(PatientDashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
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

    //show the appointment booking dialog
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

        //date selection
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                selectedDate[0] = year + "-" + (month + 1) + "-" + dayOfMonth;
                selectDateButton.setText(selectedDate[0]);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        //confirm the appointment booking
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

    //book the appointment and save it to firestore
    private void bookAppointment(String doctorId, String date, String time) {
        String patientId = mAuth.getCurrentUser().getUid();
        Appointment appointment = new Appointment(patientId, doctorId, "Pending", date, time);
        db.collection("appointments").add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PatientDashboardActivity.this, "Appointment request sent", Toast.LENGTH_SHORT).show();
                    doctorRecyclerView.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PatientDashboardActivity.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadPrescriptions() {
        db.collection("prescriptions")
                .whereEqualTo("patientId", PATIENT_UID)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading prescriptions", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            Prescription prescription = dc.getDocument().toObject(Prescription.class);
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

    private void fetchStatus() {
        if (isStatusVisible) {
            statusTextView.setVisibility(View.GONE);
            isStatusVisible = false;
        } else {
            db.collection("patients")
                    .document(PATIENT_UID)
                    .collection("appointmentStatus")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String date = document.getString("date");
                            String time = document.getString("time");
                            String status = document.getString("status");

                            String statusMessage = "Status: " + status + "\nDate: " + date + "\nTime: " + time;
                            statusTextView.setText(statusMessage);
                            statusTextView.setVisibility(View.VISIBLE);
                            isStatusVisible = true;
                        } else {
                            statusTextView.setText("No status found.");
                            statusTextView.setVisibility(View.VISIBLE);
                            isStatusVisible = true;
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FetchStatus", "Error fetching status", e);
                    });
        }
    }

    private void fetchAssistantStatus() {
        if (isStatusVisible1) {
            statusTextView1.setVisibility(View.GONE);
            isStatusVisible1 = false;
        } else {
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
                            statusTextView1.setText("Assistant: " + message);
                            statusTextView1.setVisibility(View.VISIBLE);
                            isStatusVisible1 = true;
                        } else {
                            statusTextView1.setText("No updates from assistant.");
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
        if (ContextCompat.checkSelfPermission(PatientDashboardActivity.this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PatientDashboardActivity.this,
                    new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    private void showAboutHospitalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_about_hospital, null);
        builder.setView(view);

        Button btnContactUs = view.findViewById(R.id.btnContactUs);
        btnContactUs.setOnClickListener(v -> makePhoneCall("01310388215"));

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showHelplineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_helpline, null);
        builder.setView(view);

        Button btnCallHelpline = view.findViewById(R.id.btnCallHelpline);
        btnCallHelpline.setOnClickListener(v -> makePhoneCall("01310388215"));

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall("01310388215");
            } else {
                Toast.makeText(this, "Permission denied. Cannot make the call.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}






