package com.example.learningmate;

import java.io.Serializable;


public class User implements Serializable {
    private String userId;
    private String userName;
    private int identity;
    private String pairId;
    private String registerDate;
    public static User currentUser;

    public User(String userId, String userName, int identity, String pairId, String registerDate) {
        this.userName = userName;
        this.userId = userId;
        this.identity = identity;
        this.pairId = pairId;
        this.registerDate = registerDate;
    }

    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getPairId() {
        return pairId;
    }

    public void setPairId(String pairId) {
        this.pairId = pairId;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
}
