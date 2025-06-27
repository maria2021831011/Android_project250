package com.example.hospitalmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        ImageView profileImage = findViewById(R.id.profileImage);
        TextView profileName = findViewById(R.id.profileName);
        TextView profileTitle = findViewById(R.id.profileTitle);
        TextView profileEmail = findViewById(R.id.profileEmail);
        TextView profilePhone = findViewById(R.id.profilePhone);
        TextView profileDepartment = findViewById(R.id.profileDepartment);
        TextView profileJoinDate = findViewById(R.id.profileJoinDate);
        TextView statsApproved = findViewById(R.id.statsApproved);
        TextView statsRejected = findViewById(R.id.statsRejected);
        TextView statsTotal = findViewById(R.id.statsTotal);
        Button logoutButton = findViewById(R.id.logoutButton);


        try {
            InputStream is = getAssets().open("pf.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            profileImage.setImageBitmap(bitmap);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();

        }


        profilePhone.setText("01310388215");
        profileDepartment.setText("Administration");

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date joinDate = inputFormat.parse("01-01-2025");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            profileJoinDate.setText(outputFormat.format(joinDate));
        } catch (Exception e) {
            profileJoinDate.setText("January 2025");
        }


        statsApproved.setText("42");
        statsRejected.setText("28");
        statsTotal.setText("70");

        String userId = getIntent().getStringExtra("userId");
        if (userId == null && auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        }


        if (userId != null) {
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot doc = task.getResult();

                            profileName.setText(doc.getString("name"));
                            profileTitle.setText(doc.getString("role"));
                            profileEmail.setText(doc.getString("email"));
                        }
                    });
        }

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }
}