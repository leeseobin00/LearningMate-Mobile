package com.example.learningmate.model;

public class Problem {
    private String problem;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private int point;
    private String answer;
    private String selectAnswer;
    private int selectedId;

    public Problem(String problem, String choice1, String choice2, String choice3, String choice4, int point, String answer) {
        this.problem = problem;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.point = point;
        this.answer = answer;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSelectAnswer() {
        return selectAnswer;
    }

    public void setSelectAnswer(String selectAnswer) {
        this.selectAnswer = selectAnswer;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }
}
