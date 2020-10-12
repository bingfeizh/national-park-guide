package com.hfad.nationalparksguide.data.model;

public class Comment {
    String comment;
    String userName;
    double latitude;
    double longitude;

    public Comment(String comment, String userName, double latitude, double longitude) {
        this.comment = comment;
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Comment(String comment, String userName){
        this.comment = comment;
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
