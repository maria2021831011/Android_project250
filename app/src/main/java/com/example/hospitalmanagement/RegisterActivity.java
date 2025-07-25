package com.example.hospitalmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private TextView loginTextView;
    private Spinner roleSpinner;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        roleSpinner = findViewById(R.id.spinnerRole);
        Button registerButton = findViewById(R.id.btnRegister);
        loginTextView = findViewById(R.id.tvLogin);// Register button click
        registerButton.setOnClickListener(v -> registerUser());

        // Login link click
        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String selectedRole = roleSpinner.getSelectedItem().toString().toLowerCase(); // Get the selected role from the Spinner


        // Primitive Obsession
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
        /*// Create a Validator class
public class RegistrationValidator {
    public static boolean validateEmail(EditText emailField) {
        String email = emailField.getText().toString().trim();
        if (email.isEmpty()) {
            emailField.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email");
            return false;
        }
        return true;
    }
}

// Usage:
if (!RegistrationValidator.validateEmail(emailEditText)) return;*/



        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }



        // Firebase registration
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String role = selectedRole;

                            //data clumps
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            userData.put("role", role);
                            /*// User.java model class
public class User {
    private String uid;
    private String email;
    private String role;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("role", role);
        return map;
    }
}

// Usage:
User newUser = new User(user.getUid(), email, role);
db.collection("users").document(user.getUid()).set(newUser.toMap());*/





                            // Firestore
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish(); // Close the registration activity
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Failed to store user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}