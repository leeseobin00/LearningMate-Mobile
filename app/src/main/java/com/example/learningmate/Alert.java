package com.example.learningmate;

public class Alert {
    private String alertName;
    private String uploadDate;
    private String dueDate;
    private boolean state;
    private int viewType;

    public Alert(String alertName, String uploadDate, String dueDate, boolean state, int viewType) {
        this.alertName = alertName;
        this.uploadDate = uploadDate;
        this.dueDate = dueDate;
        this.state = state;
        this.viewType = viewType;
    }

    public Alert(String alertName, String uploadDate, int viewType){
        this.alertName = alertName;
        this.uploadDate = uploadDate;
        this.viewType = viewType;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}