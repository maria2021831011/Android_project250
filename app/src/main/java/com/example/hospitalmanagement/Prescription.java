package com.example.hospitalmanagement;

public class Prescription {
    private String doctorId;
    private String patientId;
    private String medicine;
    private String dosage;

    public Prescription() {
        // Default constructor required for Firestore
    }

    public Prescription(String doctorId, String patientId, String medicine, String dosage) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.medicine = medicine;
        this.dosage = dosage;
    }

    // Getters and Setters
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getMedicine() { return medicine; }
    public void setMedicine(String medicine) { this.medicine = medicine; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
}