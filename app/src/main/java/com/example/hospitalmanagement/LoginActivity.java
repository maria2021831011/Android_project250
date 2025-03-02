package com.example.hospitalmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
public class LoginActivity extends AppCompatActivity {//app ar screen
    private EditText emailEditText, passwordEditText;
    private boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;//login authentication er jonno
    private FirebaseFirestore db;//firestore user data access.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //firebaseauth and firestore initialize
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // initialize ui
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        ImageView togglePassword = findViewById(R.id.ivTogglePassword);
        TextView forgotPassword = findViewById(R.id.tvForgotPassword);
        TextView registerLink = findViewById(R.id.tvRegister);

        // Password visible
        togglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                togglePassword.setImageResource(R.drawable.ic_eye); //Closed eye icon
            } else {
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                togglePassword.setImageResource(R.drawable.ic_eye_open); //Open eye icon
            }
            isPasswordVisible = !isPasswordVisible;
            passwordEditText.setSelection(passwordEditText.getText().length());
        });
        // loginclick
        loginButton.setOnClickListener(v -> loginUser());
        //forgotpassworclick
        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
        // RegisterClick
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    //validate user’s email and password before login
    private void loginUser() {//get user input:
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        // email and password validate
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
            return;}
        //firebase login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // login successful,retrieve user role firestore
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
                                                            startActivity(new Intent(LoginActivity.this, DoctorDashboardActivity.class));
                                                            break;
                                                        case "patient":
                                                            startActivity(new Intent(LoginActivity.this, PatientDashboardActivity.class));
                                                            break;
                                                        case "admin":
                                                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                                            break;
                                                        default:
                                                            Toast.makeText(LoginActivity.this, "Unknown role", Toast.LENGTH_SHORT).show();
                                                    }
                                                    finish(); // Close login part
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
                        //login fail hole
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}