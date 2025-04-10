package com.example.hospitalmanagement;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    private LinearLayout reportsContainer;
    private TextView todayApprovedCount, todayRejectedCount, todayTotalCount;
    private boolean isReportVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard2);
        ImageView profileImage = findViewById(R.id.profileImage);
        initializeViews();
        setupFirebase();
        setupBottomNavigation();
        setupButtonListeners();
        loadTodayStats();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
    }

    private void initializeViews() {
        reportsContainer = findViewById(R.id.reportsContainer);
        todayApprovedCount = findViewById(R.id.todayApprovedCount);
        todayRejectedCount = findViewById(R.id.todayRejectedCount);
        todayTotalCount = findViewById(R.id.todayTotalCount);
    }

    private void setupFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {

                recreate();
                return true;
            } else if (id == R.id.nav_reports) {
                showMonthlyReports();
                return true;
            } else if (id == R.id.nav_profile) {
                openProfile();
                return true;
            }
            return false;
        });
    }

    private void setupButtonListeners() {
        findViewById(R.id.toggleReportBtn).setOnClickListener(v -> toggleReport());
        findViewById(R.id.makeReportBtn).setOnClickListener(v -> generateDetailedReport());
        findViewById(R.id.monthlyReportBtn).setOnClickListener(v -> showMonthlyReports());

        findViewById(R.id.logoutBtn).setOnClickListener(v -> logoutUser());
    }

    private void toggleReport() {
        if (!isReportVisible) {
            generateQuickReport();
        } else {
            hideReport();
        }
    }

    private void generateQuickReport() {
        reportsContainer.removeAllViews();
        addReportCard("Loading quick report...", "Fetching data...", "#4CAF50");

        db.collection("appointments")
                .whereEqualTo("doctorId", DOCTOR_UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int approved = 0, rejected = 0;
                        List<String> approvedAppointments = new ArrayList<>();
                        List<String> rejectedAppointments = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String status = doc.getString("status");
                            String info = "Date: " + doc.getString("date") +
                                    "\nTime: " + doc.getString("time");
                            if ("Approved".equals(status)) {
                                approved++;
                                approvedAppointments.add(info);
                            } else if ("Rejected".equals(status)) {
                                rejected++;
                                rejectedAppointments.add(info);
                            }
                        }

                        reportsContainer.removeAllViews();
                        String reportContent = buildQuickReportContent(approved, rejected,
                                approvedAppointments, rejectedAppointments);
                        addReportCard("Quick Report", reportContent, "#4CAF50");
                        isReportVisible = true;
                    } else {
                        addReportCard("Error", "Failed to load report data", "#F44336");
                    }
                });
    }

    private String buildQuickReportContent(int approved, int rejected,
                                           List<String> approvedAppts, List<String> rejectedAppts) {
        return "👨‍⚕️ Doctor: " + DOCTOR_UID + "\n\n" +
                "✅ Approved: " + approved + "\n" +
                formatAppointments(approvedAppts) + "\n" +
                "❌ Rejected: " + rejected + "\n" +
                formatAppointments(rejectedAppts);
    }

    private void generateDetailedReport() {
        reportsContainer.removeAllViews();
        addReportCard("Creating detailed report...", "Please wait...", "#2196F3");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        db.collection("users").document(DOCTOR_UID).get()
                .addOnSuccessListener(doctorDoc -> {
                    String doctorName = doctorDoc.getString("username");
                    db.collection("users").document(PATIENT_UID).get()
                            .addOnSuccessListener(patientDoc -> {
                                String patientName = patientDoc.getString("username");
                                processDetailedReport(calendar, currentDate, doctorName, patientName);
                            });
                });
    }

    private void processDetailedReport(Calendar calendar, String date,
                                       String doctorName, String patientName) {
        db.collection("appointments")
                .whereEqualTo("doctorId", DOCTOR_UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int approved = 0, rejected = 0;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String status = doc.getString("status");
                            if ("Approved".equals(status)) approved++;
                            else if ("Rejected".equals(status)) rejected++;
                        }
                        saveAndDisplayReport(calendar, date, doctorName, patientName, approved, rejected);
                    }
                });
    }

    private void saveAndDisplayReport(Calendar calendar, String date,
                                      String doctorName, String patientName,
                                      int approved, int rejected) {
        Map<String, Object> report = new HashMap<>();
        report.put("year", calendar.get(Calendar.YEAR));
        report.put("month", calendar.get(Calendar.MONTH) + 1);
        report.put("date", date);
        report.put("totalApproved", approved);
        report.put("totalRejected", rejected);
        report.put("doctorName", doctorName);
        report.put("patientName", patientName);
        report.put("timestamp", System.currentTimeMillis());

        db.collection("monthlyReports").add(report)
                .addOnSuccessListener(documentReference -> {
                    reportsContainer.removeAllViews();
                    String reportContent = buildDetailedReportContent(report);
                    addReportCard("Detailed Report Saved", reportContent, "#2196F3");
                });
    }

    private String buildDetailedReportContent(Map<String, Object> report) {
        return "📅 Date: " + report.get("date") + "\n" +
                "👨‍⚕️ Doctor: " + report.get("doctorName") + "\n" +
                "👤 Patient: " + report.get("patientName") + "\n" +
                "✅ Approved: " + report.get("totalApproved") + "\n" +
                "❌ Rejected: " + report.get("totalRejected");
    }

    private void showMonthlyReports() {
        reportsContainer.removeAllViews();
        addReportCard("Loading monthly reports...", "Fetching data...", "#FF9800");

        db.collection("monthlyReports")
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reportsContainer.removeAllViews();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String reportContent = buildMonthlyReportContent(doc);
                            addReportCard("Monthly Report", reportContent, "#FF9800");
                        }
                    } else {
                        addReportCard("Error", "No monthly reports found", "#F44336");
                    }
                });
    }

    private String buildMonthlyReportContent(QueryDocumentSnapshot doc) {
        return "📅 " + doc.get("year") + "-" + doc.get("month") + "-" + doc.get("date") + "\n" +
                "👨‍⚕️ " + doc.get("doctorName") + "\n" +
                "👤 " + doc.get("patientName") + "\n" +
                "✅ " + doc.get("totalApproved") + " approved\n" +
                "❌ " + doc.get("totalRejected") + " rejected";
    }

    private void addReportCard(String title, String content, String color) {
        CardView card = (CardView) LayoutInflater.from(this)
                .inflate(R.layout.report_card_layout, reportsContainer, false);

        TextView titleView = card.findViewById(R.id.cardTitle);
        TextView contentView = card.findViewById(R.id.cardContent);

        titleView.setText(title);
        contentView.setText(content);
        card.setCardBackgroundColor(Color.parseColor(color));

        reportsContainer.addView(card);
    }

    private void hideReport() {
        reportsContainer.removeAllViews();
        isReportVisible = false;
    }

    private String formatAppointments(List<String> appointments) {
        if (appointments.isEmpty()) return "No appointments\n";
        StringBuilder sb = new StringBuilder();
        for (String appt : appointments) {
            sb.append("• ").append(appt).append("\n");
        }
        return sb.toString();
    }

    private void loadTodayStats() {
        db.collection("appointments")
                .whereEqualTo("doctorId", DOCTOR_UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int approved = 0, rejected = 0;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String status = doc.getString("status");
                            if ("Approved".equals(status)) approved++;
                            else if ("Rejected".equals(status)) rejected++;
                        }
                        todayApprovedCount.setText(String.valueOf(approved));
                        todayRejectedCount.setText(String.valueOf(rejected));
                        todayTotalCount.setText(String.valueOf(approved + rejected));
                    }
                });
    }



    private void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        startActivity(intent);
    }




    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}