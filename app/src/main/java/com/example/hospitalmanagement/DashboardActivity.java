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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        welcomeText = findViewById(R.id.tvWelcome);
        frameLayout = findViewById(R.id.frameLayout);


        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String role = document.getString("role");
                            String username = document.getString("username");


                            Log.d("Firestore", "Fetched Role: " + role);


                            if (role == null || role.isEmpty()) {
                                Toast.makeText(this, "Role not assigned to this user!", Toast.LENGTH_LONG).show();
                                return;
                            }


                            welcomeText.setText("Welcome, " + username + " (" + role + ")");


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


        role = role.trim().toLowerCase();


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

                Log.e("UnknownRole", "Role: " + role + " does not match any known role.");
                Toast.makeText(this, "Error: Unknown role (" + role + ")", Toast.LENGTH_LONG).show();
                return;
        }


        if (view != null) {
            frameLayout.addView(view);
        } else {
            Toast.makeText(this, "Error loading dashboard layout", Toast.LENGTH_LONG).show();
        }
    }
}

