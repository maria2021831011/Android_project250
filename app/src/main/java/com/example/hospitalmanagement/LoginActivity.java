package com.example.hospitalmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        ImageView togglePassword = findViewById(R.id.ivTogglePassword);
        TextView forgotPassword = findViewById(R.id.tvForgotPassword);
        TextView registerLink = findViewById(R.id.tvRegister);


        togglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                togglePassword.setImageResource(R.drawable.ic_eye);
            } else {
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                togglePassword.setImageResource(R.drawable.ic_eye_open);
            }
            isPasswordVisible = !isPasswordVisible;
            passwordEditText.setSelection(passwordEditText.getText().length());
        });

        // Login button click
        loginButton.setOnClickListener(v -> loginUser());

        // Forgot password click
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        // Register link click
        registerLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        // Firebase login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                            db.collection("users").document(user.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot document = task1.getResult();
                                            if (document.exists()) {
                                                String role = document.getString("role");
                                                if (role != null) {
                                                    switch (role) {
                                                        case "doctor":

                                                            createDoctorIfNotExist(user);
                                                            startActivity(new Intent(LoginActivity.this, DoctorDashboardActivity.class));
                                                            break;
                                                        case "patient":
                                                            startActivity(new Intent(LoginActivity.this, PatientDashboardActivity.class));
                                                            break;
                                                        case "admin":
                                                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                                            break;
                                                        case "assistant":
                                                            startActivity(new Intent(LoginActivity.this, AssistantDashboardActivity.class));
                                                            break;
                                                        default:
                                                            Toast.makeText(LoginActivity.this, "Unknown role", Toast.LENGTH_SHORT).show();
                                                    }
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Role not found", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve user data: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Login failed
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void createDoctorIfNotExist(FirebaseUser user) {
        String userId = user.getUid();
        DocumentReference doctorRef = db.collection("doctors").document(userId);

        doctorRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {

                    Map<String, Object> doctorData = new HashMap<>();
                    doctorData.put("email", user.getEmail());
                    doctorData.put("username", "sajib");
                    doctorData.put("role", "doctor");
                    doctorData.put("availableSlots", Arrays.asList("10:00 AM", "2:00 PM")); // Example slots

                    doctorRef.set(doctorData)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Doctor document created successfully.");
                            })
                            .addOnFailureListener(e -> {
                                Log.w("Firestore", "Error creating doctor document", e);
                            });
                } else {
                    Log.d("Firestore", "Doctor document already exists.");
                }
            } else {
                Log.w("Firestore", "Error getting doctor document", task.getException());
            }
        });
    }
}
