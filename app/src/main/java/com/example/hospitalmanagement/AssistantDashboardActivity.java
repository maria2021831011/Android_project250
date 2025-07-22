package com.example.hospitalmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AssistantDashboardActivity extends AppCompatActivity {
    private boolean appointmentsExpanded = false;
    private Button appointmentsBtn;
    private Button toggleBusyStatusButton;
    private FirebaseAuth mAuth;
    private TextView busyStatusTextView;
    private FirebaseFirestore db;
    private Button logoutButton;
    private EditText messageInput;
    private Button sendMessageButton;
    private LinearLayout appointmentsContainer;
    private static final String ASSISTANT_UID = "ICyFihhf1xfBSWPBgtE5YxIAJFC3";

    //specutative generality
    private static final String PATIENT_UID = "au3NyjugG2Z6pJBUN1m9sn5uNo02";

    /*public class UserProvider {
    private final SharedPreferences prefs;

    public String getCurrentPatientId() {
        return prefs.getString("current_patient_id");
    }
}

// Usage:
String patientId = userProvider.getCurrentPatientId();*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_dashboard);
        mAuth = FirebaseAuth.getInstance();
        //initialize
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, Assistant!");

        db = FirebaseFirestore.getInstance();
        messageInput = findViewById(R.id.messageInput);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        toggleBusyStatusButton = findViewById(R.id.toggleBusyStatusButton);
        busyStatusTextView = findViewById(R.id.busyStatusTextView);

        busyStatusTextView.setVisibility(View.GONE);
        appointmentsContainer = findViewById(R.id.appointmentsContainer);

        toggleBusyStatusButton.setOnClickListener(v -> toggleBusyStatusVisibility());
        sendMessageButton.setOnClickListener(v -> sendMessageToPatient());

        listenForBusyStatus();
        listenForAppointments();
        appointmentsBtn = findViewById(R.id.appointmentsBtn);
        appointmentsBtn.setOnClickListener(v -> toggleAppointmentsView());

        appointmentsContainer.setVisibility(View.GONE);
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(AssistantDashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
    }



    private void toggleAppointmentsView() {
        appointmentsExpanded = !appointmentsExpanded;
        if (appointmentsExpanded) {
            appointmentsContainer.setVisibility(View.VISIBLE);
            appointmentsBtn.setText("Hide Appointments");
        } else {
            appointmentsContainer.setVisibility(View.GONE);
            appointmentsBtn.setText("Show Appointments");
        }
    }
    // appointment btn click korle sb kisu
    private void listenForAppointments() {
        db.collection("appointments")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("AssistantDashboard", "Error loading appointments", error);
                        Toast.makeText(this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    appointmentsContainer.removeAllViews();

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {

                            String status = document.getString("status");


                            String patientName = "Riaz";
                            String doctorName = "Sajib";
                            String timeRange = "11 AM to 2 PM";

                            //button for each appointment
                            Button appointmentBtn = new Button(this);
                            appointmentBtn.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            //set button text with appointment details
                            String btnText = "Patient: " + patientName +
                                    "\nStatus: " + (status != null ? status : "Pending") +
                                    "\nTime: " + timeRange +
                                    "\nDoctor: " + doctorName;
                            appointmentBtn.setText(btnText);

                            //set button style based on status
                            String displayStatus = status != null ? status : "Pending";
                            switch (displayStatus) {
                                case "Pending":
                                    appointmentBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_corners_yellow));
                                    break;
                                case "Confirmed":
                                    appointmentBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_corners_green));
                                    break;
                                case "Cancelled":
                                    appointmentBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_corners_red));
                                    break;
                                default:
                                    appointmentBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.card_gradient_green));
                            }
                           //Shotgun Surgery
                            appointmentBtn.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                            appointmentBtn.setPadding(16, 16, 16, 16);
                            appointmentBtn.setTextSize(14);
                            appointmentBtn.setAllCaps(false);
                            appointmentBtn.setElevation(4);

                            /*public final class ButtonStyles {
    public static void applyAppointmentStyle(Button button) {
                                 appointmentBtn.setPadding(16, 16, 16, 16);
                            appointmentBtn.setTextSize(14);
                            appointmentBtn.setAllCaps(false);
                            appointmentBtn.setElevation(4);}
}

// Usage:
ButtonStyles.applyAppointmentStyle(appointmentBtn);*/





                            //margin between buttons
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) appointmentBtn.getLayoutParams();
                            params.setMargins(0, 0, 0, 16);
                            appointmentBtn.setLayoutParams(params);

                            //button to container
                            appointmentsContainer.addView(appointmentBtn);
                        }
                    } else {
                        TextView noAppointmentsText = new TextView(this);
                        noAppointmentsText.setText("No appointments found");
                        noAppointmentsText.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                        noAppointmentsText.setTextSize(16);
                        noAppointmentsText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        appointmentsContainer.addView(noAppointmentsText);
                    }
                });
    }

    private void toggleBusyStatusVisibility() {
        if (busyStatusTextView.getVisibility() == View.GONE) {
            busyStatusTextView.setVisibility(View.VISIBLE);
        } else {
            busyStatusTextView.setVisibility(View.GONE);
        }
    }

    private void listenForBusyStatus() {
        db.collection("doctors")
                .whereEqualTo("busy", true)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("AssistantDashboard", "Error loading busy status", error);
                        Toast.makeText(this, "Error loading busy status", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                            Map<String, Object> doctorData = dc.getDocument().getData();
                            String busyStartTime = (String) doctorData.get("busyStartTime");
                            String busyEndTime = (String) doctorData.get("busyEndTime");

                            //update  the busy status
                            if (busyStartTime != null && busyEndTime != null) {
                                busyStatusTextView.setText("Doctor is busy from " + busyStartTime + " to " + busyEndTime);
                            } else {
                                busyStatusTextView.setText("Doctor is busy, but no time range specified.");
                            }
                        }
                    } else {
                        busyStatusTextView.setText("No doctors are busy at the moment.");
                    }
                });
    }

    private void sendMessageToPatient() {
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        //message for the patient
        Map<String, Object> statusMessage = new HashMap<>();
        statusMessage.put("message", message);
        statusMessage.put("timestamp", FieldValue.serverTimestamp());
//Inappropriate Intimacy


        db.collection("assistantStatus")
                .document(PATIENT_UID)
                .collection("messages")
                .add(statusMessage)

/* /*public class MessageRepository {
    private final FirebaseFirestore db;

    public void sendToPatient(String patientId, String message) {
        // Encapsulates Firestore structure
        db.collection("assistantStatus")
          .document(patientId)
          .collection("messages")
          .add(createMessage(message));
    }
}

messageRepository.sendToPatient(patientId, message);*/


                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Message sent to patient", Toast.LENGTH_SHORT).show();
                    messageInput.setText("");
                })
                .addOnFailureListener(e -> {
                    Log.e("AssistantDashboard", "Failed to send message", e);
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }
}

