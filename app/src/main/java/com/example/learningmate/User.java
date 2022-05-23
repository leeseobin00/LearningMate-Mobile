package com.example.learningmate;

public class User {
    String userName;
    String userId;


    public User(String userName, String userId){
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }
}
