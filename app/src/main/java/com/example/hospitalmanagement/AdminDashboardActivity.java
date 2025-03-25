package com.example.hospitalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button logoutBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView monthlyReportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard2); // Ensure correct XML is used

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        logoutBtn = findViewById(R.id.logoutBtn);
        monthlyReportText = findViewById(R.id.monthlyReportText); // Make sure this ID matches

        // Set up logout button
        logoutBtn.setOnClickListener(v -> logoutUser());

        // Generate monthly report
        generateMonthlyReport();
    }

    private void logoutUser() {
        mAuth.signOut(); // Log out
        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void generateMonthlyReport() {
        // Get the current month and year
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String currentMonth = dateFormat.format(calendar.getTime());

        // Query appointments for the current month
        db.collection("appointments")
                .whereGreaterThanOrEqualTo("date", currentMonth + "-01")
                .whereLessThanOrEqualTo("date", currentMonth + "-31")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int totalAppointments = task.getResult().size();
                        int completedAppointments = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if ("Completed".equals(document.getString("status"))) {
                                completedAppointments++;
                            }
                        }

                        // Query total patients and doctors
                        int finalCompletedAppointments = completedAppointments;
                        db.collection("users")
                                .whereEqualTo("role", "patient")
                                .get()
                                .addOnCompleteListener(patientTask -> {
                                    if (patientTask.isSuccessful()) {
                                        int totalPatients = patientTask.getResult().size();

                                        db.collection("users")
                                                .whereEqualTo("role", "doctor")
                                                .get()
                                                .addOnCompleteListener(doctorTask -> {
                                                    if (doctorTask.isSuccessful()) {
                                                        int totalDoctors = doctorTask.getResult().size();

                                                        // Generate the report
                                                        String report = "Monthly Report (" + currentMonth + "):\n" +
                                                                "Total Patients: " + totalPatients + "\n" +
                                                                "Total Doctors: " + totalDoctors + "\n" +
                                                                "Total Appointments: " + totalAppointments + "\n" +
                                                                "Completed Appointments: " + finalCompletedAppointments;

                                                        // Display the report
                                                        monthlyReportText.setText(report);
                                                    } else {
                                                        monthlyReportText.setText("Error loading doctors");
                                                    }
                                                });
                                    } else {
                                        monthlyReportText.setText("Error loading patients");
                                    }
                                });
                    } else {
                        monthlyReportText.setText("Error loading appointments");
                    }
                });
    }
}
