package com.example.hospitalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity {

    private static final String PATIENT_UID = "au3NyjugG2Z6pJBUN1m9sn5uNo02";
    private static final String DOCTOR_UID = "AgsP6s8GciMxwmeeegMLSmRGHBF3";

    private FirebaseFirestore db;
    private TextView reportTextView;
    private Button toggleReportBtn;
    private Button makeReportBtn;
    private Button monthlyReportBtn;
    private Button logoutBtn;
    private boolean isReportVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard2);

        db = FirebaseFirestore.getInstance();
        reportTextView = findViewById(R.id.reportTextView);
        toggleReportBtn = findViewById(R.id.toggleReportBtn);
        makeReportBtn = findViewById(R.id.makeReportBtn);
        monthlyReportBtn = findViewById(R.id.monthlyReportBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        toggleReportBtn.setOnClickListener(v -> {
            if (!isReportVisible) {
                generateQuickReport();
            } else {
                hideReport();
            }
        });

        makeReportBtn.setOnClickListener(v -> generateDetailedReport());
        monthlyReportBtn.setOnClickListener(v -> showMonthlyReports());
        logoutBtn.setOnClickListener(v -> logoutUser());
    }

    private void generateQuickReport() {
        reportTextView.setText("Loading quick report...");
        isReportVisible = true;
        toggleReportBtn.setText("Hide Report");

        db.collection("appointments")
                .whereEqualTo("doctorId", DOCTOR_UID)
                .get()
                .addOnCompleteListener(doctorTask -> {
                    if (doctorTask.isSuccessful()) {
                        List<String> doctorApproved = new ArrayList<>();
                        List<String> doctorRejected = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : doctorTask.getResult()) {
                            String status = doc.getString("status");
                            String info = "Date: " + doc.getString("date") +
                                    "\nTime: " + doc.getString("time");

                            if ("Approved".equals(status)) {
                                doctorApproved.add(info);
                            } else if ("Rejected".equals(status)) {
                                doctorRejected.add(info);
                            }
                        }

                        String report = "🏥 Quick Report\n\n" +
                                "👨‍⚕️ Doctor ID: " + DOCTOR_UID + "\n" +
                                "✅ Approved: " + doctorApproved.size() + "\n" +
                                formatAppointments(doctorApproved) + "\n" +
                                "❌ Rejected: " + doctorRejected.size() + "\n" +
                                formatAppointments(doctorRejected);

                        reportTextView.setText(report);
                    } else {
                        reportTextView.setText("Error loading doctor data");
                    }
                });
    }

    private void generateDetailedReport() {
        reportTextView.setText("Creating detailed report...");
        isReportVisible = true;

        // Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        // Get doctor and patient names
        db.collection("users").document(DOCTOR_UID).get()
                .addOnSuccessListener(doctorDoc -> {
                    String doctorName = doctorDoc.getString("username");

                    db.collection("users").document(PATIENT_UID).get()
                            .addOnSuccessListener(patientDoc -> {
                                String patientName = patientDoc.getString("username");

                                // Get appointment stats
                                db.collection("appointments")
                                        .whereEqualTo("doctorId", DOCTOR_UID)
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                int approved = 0;
                                                int rejected = 0;

                                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                                    String status = doc.getString("status");
                                                    if ("Approved".equals(status)) approved++;
                                                    else if ("Rejected".equals(status)) rejected++;
                                                }

                                                // Create report data
                                                Map<String, Object> report = new HashMap<>();
                                                report.put("year", calendar.get(Calendar.YEAR));
                                                report.put("month", calendar.get(Calendar.MONTH) + 1);
                                                report.put("date", currentDate);
                                                report.put("totalApproved", approved);
                                                report.put("totalRejected", rejected);
                                                report.put("doctorName", doctorName);
                                                report.put("patientName", patientName);
                                                report.put("timestamp", System.currentTimeMillis());

                                                // Save to Firestore
                                                int finalApproved = approved;
                                                int finalRejected = rejected;
                                                db.collection("monthlyReports").add(report)
                                                        .addOnSuccessListener(documentReference -> {
                                                            String detailedReport = "📝 Detailed Report Saved\n\n" +
                                                                    "Year: " + report.get("year") + "\n" +
                                                                    "Month: " + report.get("month") + "\n" +
                                                                    "Date: " + report.get("date") + "\n" +
                                                                    "Doctor: " + doctorName + "\n" +
                                                                    "Patient: " + patientName + "\n" +
                                                                    "✅ Approved: " + finalApproved + "\n" +
                                                                    "❌ Rejected: " + finalRejected;
                                                            reportTextView.setText(detailedReport);
                                                        });
                                            }
                                        });
                            });
                });
    }

    private void showMonthlyReports() {
        reportTextView.setText("Loading monthly reports...");
        isReportVisible = true;

        db.collection("monthlyReports")
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder reports = new StringBuilder("📅 Monthly Reports\n\n");
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            reports.append("📅 ")
                                    .append(doc.get("year")).append("-")
                                    .append(doc.get("month")).append("-")
                                    .append(doc.get("date")).append("\n")
                                    .append("👨‍⚕️ ").append(doc.get("doctorName")).append("\n")
                                    .append("👤 ").append(doc.get("patientName")).append("\n")
                                    .append("✅ ").append(doc.get("totalApproved")).append("  ")
                                    .append("❌ ").append(doc.get("totalRejected")).append("\n\n");
                        }
                        reportTextView.setText(reports.toString());
                    } else {
                        reportTextView.setText("No monthly reports found");
                    }
                });
    }

    private void hideReport() {
        reportTextView.setText("");
        isReportVisible = false;
        toggleReportBtn.setText("Show Report");
    }

    private String formatAppointments(List<String> appointments) {
        if (appointments.isEmpty()) return "No appointments\n";
        StringBuilder sb = new StringBuilder();
        for (String appt : appointments) {
            sb.append("• ").append(appt).append("\n");
        }
        return sb.toString();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}