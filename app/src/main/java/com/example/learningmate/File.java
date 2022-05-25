package com.example.learningmate;

public class File {
    public String fileName;
    public int attemptNum;
    public String status;
    public int year;
    public int month;
    public int day;
    public int score;

    public File(String fileName, String status, int attemptNum, int year, int month, int day, int score){
        this.fileName = fileName;
        this.attemptNum = attemptNum;
        this.status = status;
        this.year = year;
        this.month = month;
        this.day = day;
        this.score = score;
    }
}