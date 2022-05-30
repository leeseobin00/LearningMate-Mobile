package com.example.learningmate;

public class AlertMentor {
    public String alertName;
    public String date;

    public AlertMentor(String alertName, String date){
        this.alertName = alertName;
        this.date = date;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}