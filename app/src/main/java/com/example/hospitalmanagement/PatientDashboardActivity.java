package com.example.hospitalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity {

    private RecyclerView dashboardRecyclerView;
    private Button logoutBtn, homeBtn;
    private FirebaseAuth mAuth;
    private DashboardAdapter adapter;
    private List<DashboardOption> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        mAuth = FirebaseAuth.getInstance();
        homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(v -> toggleDashboardVisibility());
        dashboardRecyclerView = findViewById(R.id.dashboard_recycler_view);
        dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set up Dashboard Options
        options = new ArrayList<>();
        options.add(new DashboardOption("Appointments", R.drawable.ic_appointments));
        options.add(new DashboardOption("Prescriptions", R.drawable.ic_prescriptions));
        options.add(new DashboardOption("Doctors", R.drawable.ic_doctors));
        options.add(new DashboardOption("Medical Records", R.drawable.ic_medical_records));
        options.add(new DashboardOption("Upload Documents", R.drawable.ic_upload));
        options.add(new DashboardOption("Edit Profile", R.drawable.ic_edit_profile));
        options.add(new DashboardOption("Blog", R.drawable.ic_blog));
        options.add(new DashboardOption("Contact Us", R.drawable.ic_contact));
        options.add(new DashboardOption("About Developer", R.drawable.ic_about));
        adapter = new DashboardAdapter(options);
        dashboardRecyclerView.setAdapter(adapter);
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> logoutUser());
    }

    // Toggle Dashboard Visibility
    private void toggleDashboardVisibility() {
        if (dashboardRecyclerView.getVisibility() == View.GONE) {
            dashboardRecyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            dashboardRecyclerView.setVisibility(View.GONE);
        }
    }
    // Logout User
    private void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(PatientDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        finish();
    }
}