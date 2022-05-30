package com.example.learningmate.model;

import java.io.Serializable;

public class Submission implements Serializable {
    private String submitId;
    private String assignId;
    private String submitterUid;
    private String submitDate;
    private String submitFileId;

    public Submission(){

    }
    public Submission(String submitId, String assignId, String submitterUid, String submitDate, String submitFileId) {
        this.submitId = submitId;
        this.assignId = assignId;
        this.submitterUid = submitterUid;
        this.submitDate = submitDate;
        this.submitFileId = submitFileId;
    }

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getSubmitterUid() {
        return submitterUid;
    }

    public void setSubmitterUid(String submitterUid) {
        this.submitterUid = submitterUid;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getSubmitFileId() {
        return submitFileId;
    }

    public void setSubmitFileId(String submitFileId) {
        this.submitFileId = submitFileId;
    }
}
