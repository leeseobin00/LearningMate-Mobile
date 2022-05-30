package com.example.learningmate.model;

import java.io.Serializable;

public class Quiz implements Serializable {
    private String quizId;
    private String quizOwner;
    private String title;
    private int timeLimit;
    private int quizGrade;
    private int perfectScore;

    public Quiz(String quizId, String quizOwner, String title, int timeLimit, int quizGrade, int perfectScore) {
        this.quizId = quizId;
        this.quizOwner = quizOwner;
        this.title = title;
        this.timeLimit = timeLimit;
        this.quizGrade = quizGrade;
        this.perfectScore = perfectScore;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizOwner() {
        return quizOwner;
    }

    public void setQuizOwner(String quizOwner) {
        this.quizOwner = quizOwner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getQuizGrade() {
        return quizGrade;
    }

    public void setQuizGrade(int quizGrade) {
        this.quizGrade = quizGrade;
    }

    public int getPerfectScore() {
        return perfectScore;
    }

    public void setPerfectScore(int perfectScore) {
        this.perfectScore = perfectScore;
    }
}

