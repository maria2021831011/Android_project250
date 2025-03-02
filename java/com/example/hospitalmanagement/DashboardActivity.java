package com.example.hospitalmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private FrameLayout frameLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI
        welcomeText = findViewById(R.id.tvWelcome);
        frameLayout = findViewById(R.id.frameLayout);

        // Fetch user role from Firestore
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String role = document.getString("role");
                            String username = document.getString("username");

                            // Log role for debugging purposes
                            Log.d("Firestore", "Fetched Role: " + role);

                            // Check if role is missing
                            if (role == null || role.isEmpty()) {
                                Toast.makeText(this, "Role not assigned to this user!", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Display welcome message
                            welcomeText.setText("Welcome, " + username + " (" + role + ")");

                            // Load role-specific UI
                            loadRoleSpecificUI(role);
                        } else {
                            Toast.makeText(this, "User data not found in Firestore", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "Error fetching user data", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadRoleSpecificUI(String role) {
        frameLayout.removeAllViews();
        View view = null;

        // Normalize role by trimming and making it lowercase
        role = role.trim().toLowerCase();

        // Switch based on the normalized role
        switch (role) {
            case "admin":
                view = getLayoutInflater().inflate(R.layout.activity_admin_dashboard2, frameLayout, false);
                break;
            case "doctor":
                view = getLayoutInflater().inflate(R.layout.activity_doctor_dashboard, frameLayout, false);
                break;

            case "patient":
                view = getLayoutInflater().inflate(R.layout.activity_patient_dashboard, frameLayout, false);
                break;
            default:
                // Log the unknown role for debugging
                Log.e("UnknownRole", "Role: " + role + " does not match any known role.");
                Toast.makeText(this, "Error: Unknown role (" + role + ")", Toast.LENGTH_LONG).show();
                return;
        }

        // Check if the view is valid and add it to the frame layout
        if (view != null) {
            frameLayout.addView(view);
        } else {
            Toast.makeText(this, "Error loading dashboard layout", Toast.LENGTH_LONG).show();
        }
    }
}

