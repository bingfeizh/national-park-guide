package com.hfad.nationalparksguide.data.model;

public class User {
    String userEmail;

    public User(){

    }

    public User(String userEmail){
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }



    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
