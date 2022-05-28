package com.example.learningmate;

import java.io.Serializable;

public class Homework implements Serializable {
    private String homeworkId;
    private String mentorUid;
    private String assignDate;
    private String title;
    private String body;
    private int gradedScore;
    private int perfectScore;
    private int fileId;
    private String submitId;
    private String dueDate;

    public Homework(){

    }

    public Homework(String homeworkId, String mentorUid, String assignDate, String title, String body, int gradedScore, int perfectScore, int fileId, String submitId, String dueDate) {
        this.homeworkId = homeworkId;
        this.mentorUid = mentorUid;
        this.assignDate = assignDate;
        this.title = title;
        this.body = body;
        this.gradedScore = gradedScore;
        this.perfectScore = perfectScore;
        this.fileId = fileId;
        this.submitId = submitId;
        this.dueDate = dueDate;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getMentorUid() {
        return mentorUid;
    }

    public void setMentorUid(String mentorUid) {
        this.mentorUid = mentorUid;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getGradedScore() {
        return gradedScore;
    }

    public void setGradedScore(int gradedScore) {
        this.gradedScore = gradedScore;
    }

    public int getPerfectScore() {
        return perfectScore;
    }

    public void setPerfectScore(int perfectScore) {
        this.perfectScore = perfectScore;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }
}
