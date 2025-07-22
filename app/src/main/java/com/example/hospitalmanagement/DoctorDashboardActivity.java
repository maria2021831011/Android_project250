package com.example.hospitalmanagement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorDashboardActivity extends AppCompatActivity {

    private RecyclerView appointmentRecyclerView;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList;
    private List<Appointment> completedAppointmentList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String doctorId;
    private Button appointmentButton, completedAppointmentButton, busyStatusButton, logoutBtn, prescribeButton;
    private boolean isBusy = false;
    private Button sendStatusButton;
    private TextView doctorName, doctorSpecialty, doctorStatus, appointmentsCount, patientsCount, ratingValue;

    private String busyStartTime, busyEndTime;
    private static final String ASSISTANT_UID = "ICyFihhf1xfBSWPBgtE5YxIAJFC3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        doctorId = mAuth.getCurrentUser().getUid();
        doctorName = findViewById(R.id.doctorName);
        doctorSpecialty = findViewById(R.id.doctorSpecialty);
        doctorStatus = findViewById(R.id.doctorStatus);
        appointmentsCount = findViewById(R.id.appointmentsCount);
        patientsCount = findViewById(R.id.patientsCount);
        ratingValue = findViewById(R.id.ratingValue);
        appointmentRecyclerView = findViewById(R.id.appointmentRecyclerView);
        appointmentButton = findViewById(R.id.appointmentButton);
        completedAppointmentButton = findViewById(R.id.completedAppointmentButton);
        busyStatusButton = findViewById(R.id.busyStatusButton);
        logoutBtn = findViewById(R.id.logoutBtn);
        prescribeButton = findViewById(R.id.prescribeButton);
        sendStatusButton = findViewById(R.id.sendStatusButton);
        loadDoctorProfile();
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        completedAppointmentList = new ArrayList<>();
//recycle part a add korlm reject ,approved
        appointmentAdapter = new AppointmentAdapter(appointmentList, new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onApprove(Appointment appointment) {
                updateAppointmentStatus(appointment, "Approved");
            }
            @Override
            public void onReject(Appointment appointment) {
                updateAppointmentStatus(appointment, "Rejected");
            }
        });
        appointmentRecyclerView.setAdapter(appointmentAdapter);

        appointmentRecyclerView.setVisibility(View.GONE);

        // doctor part er sobgula button jonno kaj
        appointmentButton.setOnClickListener(v -> {
            if (appointmentRecyclerView.getVisibility() == View.GONE) {
                appointmentRecyclerView.setVisibility(View.VISIBLE);
                loadPendingAppointments();
            } else {
                appointmentRecyclerView.setVisibility(View.GONE);
            }
        });

        busyStatusButton.setOnClickListener(v -> showTimePickerDialog());
        completedAppointmentButton.setOnClickListener(v -> {
            if (appointmentRecyclerView.getVisibility() == View.GONE) {
                appointmentRecyclerView.setVisibility(View.VISIBLE);
                loadCompletedAppointments();
            } else {
                appointmentRecyclerView.setVisibility(View.GONE);
            }
        });

        prescribeButton.setOnClickListener(v -> showPrescriptionDialog());
        sendStatusButton.setOnClickListener(v -> showStatusDialog());
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(DoctorDashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
//profile er sobkisu

    //long method problem
    private void loadDoctorProfile() {
        db.collection("doctors").document(doctorId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String specialty = documentSnapshot.getString("specialty");
                        boolean isBusy = documentSnapshot.getBoolean("busy");
                        Long appointments = documentSnapshot.getLong("appointmentsCount");
                        Long patients = documentSnapshot.getLong("patientsCount");
                        Double rating = documentSnapshot.getDouble("rating");

                        doctorName.setText(name != null ? name : "Dr.Sajib");
                        doctorSpecialty.setText(specialty != null ? specialty : "Cardiologist");
                        doctorStatus.setText(isBusy ? "Busy" : "Available");
                        doctorStatus.setTextColor(isBusy ? getResources().getColor(R.color.red_500) :
                                getResources().getColor(R.color.green_500));
                        doctorStatus.setCompoundDrawablesWithIntrinsicBounds(
                                isBusy ? R.drawable.ic_circle_red : R.drawable.ic_circle_green,
                                0, 0, 0);

                        appointmentsCount.setText(appointments != null ? String.valueOf(appointments) : "40");
                        patientsCount.setText(patients != null ? String.valueOf(patients) : "1");
                        ratingValue.setText(rating != null ? String.format("%.1f", rating) : "4.0");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                });
    }



//extract method
    /*private void loadDoctorProfile() {
    db.collection("doctors").document(doctorId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    updateDoctorUI(documentSnapshot);
                }
            })
            .addOnFailureListener(e -> showToast("Failed to load profile"));
}

private void updateDoctorUI(DocumentSnapshot documentSnapshot) {
    String name = documentSnapshot.getString("name");
    String specialty = documentSnapshot.getString("specialty");
    boolean isBusy = documentSnapshot.getBoolean("busy");
    Long appointments = documentSnapshot.getLong("appointmentsCount");
    Long patients = documentSnapshot.getLong("patientsCount");
    Double rating = documentSnapshot.getDouble("rating");

    setDoctorBasicInfo(name, specialty);
    setDoctorStatus(isBusy);
    setDoctorStats(appointments, patients, rating);
}

private void setDoctorBasicInfo(String name, String specialty) {
    doctorName.setText(name != null ? name : "Dr.Sajib");
    doctorSpecialty.setText(specialty != null ? specialty : "Cardiologist");
}

private void setDoctorStatus(boolean isBusy) {
    doctorStatus.setText(isBusy ? "Busy" : "Available");
    doctorStatus.setTextColor(isBusy ? getColor(R.color.red_500) : getColor(R.color.green_500));
    doctorStatus.setCompoundDrawablesWithIntrinsicBounds(
            isBusy ? R.drawable.ic_circle_red : R.drawable.ic_circle_green, 0, 0, 0);
}

private void setDoctorStats(Long appointments, Long patients, Double rating) {
    appointmentsCount.setText(appointments != null ? String.valueOf(appointments) : "40");
    patientsCount.setText(patients != null ? String.valueOf(patients) : "1");
    ratingValue.setText(rating != null ? String.format("%.1f", rating) : "4.0");
}

*/





//apppointment ane firestore theke
    private void loadPendingAppointments() {
        db.collection("appointments")
                .whereEqualTo("doctorId", doctorId)
                .whereEqualTo("status", "Pending")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(DoctorDashboardActivity.this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    appointmentList.clear();
                    if (querySnapshot != null) {
                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            Appointment appointment = dc.getDocument().toObject(Appointment.class);
                            appointment.setId(dc.getDocument().getId());
                            appointmentList.add(appointment);
                        }
                    }
                    appointmentAdapter.notifyDataSetChanged();
                    if (appointmentList.isEmpty()) {
                        Toast.makeText(DoctorDashboardActivity.this, "No pending appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCompletedAppointments() {
        db.collection("appointments")
                .whereEqualTo("doctorId", doctorId)
                .whereEqualTo("status", "Approved")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(DoctorDashboardActivity.this, "Error loading completed appointments", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    completedAppointmentList.clear();
                    if (querySnapshot != null) {
                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            Appointment appointment = dc.getDocument().toObject(Appointment.class);
                            appointment.setId(dc.getDocument().getId());
                            completedAppointmentList.add(appointment);
                        }
                    }
                    appointmentAdapter.updateAppointments(completedAppointmentList);
                    appointmentAdapter.notifyDataSetChanged();
                    if (completedAppointmentList.isEmpty()) {
                        Toast.makeText(DoctorDashboardActivity.this, "No completed appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateAppointmentStatus(Appointment appointment, String newStatus) {
        db.collection("appointments")
                .document(appointment.getId())
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DoctorDashboardActivity.this, "Appointment " + newStatus, Toast.LENGTH_SHORT).show();
                    if (newStatus.equals("Rejected")) {
                        appointmentList.remove(appointment);
                        appointmentAdapter.notifyDataSetChanged();
                    } else if (newStatus.equals("Approved")) {
                        appointmentList.remove(appointment);
                        completedAppointmentList.add(appointment);
                        appointmentAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DoctorDashboardActivity.this, "Failed to update appointment", Toast.LENGTH_SHORT).show();
                });
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorDashboardActivity.this, (view, hourOfDay, minute) -> {
            if (busyStartTime == null) {
                busyStartTime = formatTime(hourOfDay, minute);
                Toast.makeText(DoctorDashboardActivity.this, "Start time set to " + busyStartTime, Toast.LENGTH_SHORT).show();
                showEndTimePickerDialog();
            }
        }, 12, 0, true);
        timePickerDialog.show();
    }



    private void showEndTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorDashboardActivity.this, (view, hourOfDay, minute) -> {
            busyEndTime = formatTime(hourOfDay, minute);
            Toast.makeText(DoctorDashboardActivity.this, "End time set to " + busyEndTime, Toast.LENGTH_SHORT).show();
            isBusy = true;
            updateBusyStatus();
        }, 12, 0, true);
        timePickerDialog.show();
    }

    private String formatTime(int hour, int minute) {
        String amPm = (hour < 12) ? "AM" : "PM";
        if (hour > 12) hour -= 12;
        return String.format("%02d:%02d %s", hour, minute, amPm);
    }
//doctor tar status update korlo busy kre diye
    private void updateBusyStatus() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("busy", isBusy);
        updates.put("busyStartTime", busyStartTime);
        updates.put("busyEndTime", busyEndTime);

        db.collection("doctors").document(doctorId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Doctor is now marked as busy", Toast.LENGTH_SHORT).show();
                    doctorStatus.setText("Busy");
                    doctorStatus.setTextColor(getResources().getColor(R.color.red_500));
                    doctorStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_red, 0, 0, 0);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show());
    }

    @SuppressLint("SetTextI18n")
    private void showPrescriptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_prescribe_medicine, null);
        builder.setView(view);

        EditText patientIdEditText = view.findViewById(R.id.patientIdEditText);
        EditText medicineEditText = view.findViewById(R.id.medicineEditText);
        EditText dosageEditText = view.findViewById(R.id.dosageEditText);
        Button prescribeButton = view.findViewById(R.id.prescribeButton);

        patientIdEditText.setText("au3NyjugG2Z6pJBUN1m9sn5uNo02");
        patientIdEditText.setEnabled(false);

        AlertDialog dialog = builder.create();
        dialog.show();
//jokhon prescribe btn click kora hy .jevbe handle kore .
        prescribeButton.setOnClickListener(v -> {
            String patientId = patientIdEditText.getText().toString();
            String medicine = medicineEditText.getText().toString();
            String dosage = dosageEditText.getText().toString();

            if (medicine.isEmpty() || dosage.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Prescription prescription = new Prescription(doctorId, patientId, medicine, dosage);
            db.collection("prescriptions").add(prescription)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Prescription sent", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to send prescription", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void showStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_send_status, null);
        builder.setView(view);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText dateEditText = view.findViewById(R.id.dateEditText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText timeEditText = view.findViewById(R.id.timeEditText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button selectDateButton = view.findViewById(R.id.selectDateButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button selectTimeButton = view.findViewById(R.id.selectTimeButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button approveButton = view.findViewById(R.id.approveButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button rejectButton = view.findViewById(R.id.rejectButton);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Date Picker
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                dateEditText.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Time Picker
        selectTimeButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                String selectedTime = formatTime(hourOfDay, minute);
                timeEditText.setText(selectedTime);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        // Approve Button
        approveButton.setOnClickListener(v -> {
            String date = dateEditText.getText().toString();
            String time = timeEditText.getText().toString();
            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
                return;
            }
            sendStatusToPatient(date, time, "Approved");
            dialog.dismiss();
        });

        // Reject Button
        rejectButton.setOnClickListener(v -> {
            String date = dateEditText.getText().toString();
            String time = timeEditText.getText().toString();
            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
                return;
            }
            sendStatusToPatient(date, time, "Rejected");
            dialog.dismiss();
        });
    }

    private void sendStatusToPatient(String date, String time, String status) {
        Map<String, Object> statusData = new HashMap<>();
        statusData.put("date", date);
        statusData.put("time", time);
        statusData.put("status", status);
        statusData.put("timestamp", System.currentTimeMillis());

        db.collection("patients")
                .document("au3NyjugG2Z6pJBUN1m9sn5uNo02")
                .collection("appointmentStatus")
                .add(statusData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Status sent to patient", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("SendStatus", "Error sending status", e);
                });
    }
}