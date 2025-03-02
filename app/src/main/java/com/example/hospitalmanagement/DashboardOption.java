package com.example.hospitalmanagement;

public class DashboardOption {
    private String title;
    private int iconResId;

    public DashboardOption(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }


    public int getIconResId() {
        return iconResId;
    }
}