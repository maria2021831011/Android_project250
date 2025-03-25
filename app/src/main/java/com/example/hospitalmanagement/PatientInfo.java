package com.example.hospitalmanagement;

public class PatientInfo {

    private String name;
    private String time;
    private String email;
    private String phone;

    // Constructor
    public PatientInfo(String name, String time, String email, String phone) {
        this.name = name;
        this.time = time;
        this.email = email;
        this.phone = phone;
    }

    // Default constructor for Firebase (needed for deserialization)
    public PatientInfo() {
    }

    // Getter and Setter methods for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
