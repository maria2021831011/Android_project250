package com.example.hospitalmanagement;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;
import java.util.ArrayList;

public class ViewDoctorsActivity extends AppCompatActivity {

    private ListView doctorsListView;
    private List<String> doctorsList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctors);

        doctorsListView = findViewById(R.id.lvDoctors);
        db = FirebaseFirestore.getInstance();
        doctorsList = new ArrayList<>();

        // Load available doctors from Firestore
        loadDoctors();
    }

    private void loadDoctors() {
        db.collection("doctors")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        if (!result.isEmpty()) {
                            for (DocumentSnapshot document : result) {
                                String doctorName = document.getString("username"); // Assuming 'username' stores the doctor's name
                                doctorsList.add(doctorName);
                            }

                            // Set the list of doctors
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorsList);
                            doctorsListView.setAdapter(adapter);
                        } else {
                            Toast.makeText(ViewDoctorsActivity.this, "No doctors found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ViewDoctorsActivity.this, "Error loading doctors", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
