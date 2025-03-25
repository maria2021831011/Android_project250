package com.example.hospitalmanagement;

public class Appointment {

    private String id;       // Firestore document ID
    private String patientId;
    private String doctorId;
    private String status;   // "Pending", "Approved", "Rejected"
    private String date;
    private String time;

    // Empty constructor required for Firestore
    public Appointment() { }

    public Appointment(String patientId, String doctorId, String status, String date, String time) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public String getDoctorId() {


        return doctorId;
    }
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}