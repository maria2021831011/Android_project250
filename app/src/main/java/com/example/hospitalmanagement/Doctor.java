package com.example.hospitalmanagement;

import java.util.List;

public class Doctor {
    private String uid;
    private String username;
    private String email;
    private String role;
    private List<String> availableSlots;

    //  constructor required for firestore
    public Doctor() { }

    public Doctor(String username, String email, String role, List<String> availableSlots) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.availableSlots = availableSlots;
    }

    // getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<String> availableSlots) {
        this.availableSlots = availableSlots;
    }
}
