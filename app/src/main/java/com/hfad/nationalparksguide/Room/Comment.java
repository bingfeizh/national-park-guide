package com.hfad.nationalparksguide.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Comment {
    @PrimaryKey
    @NonNull
    public String uid;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "comment")
    public String comment;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "latitude")
    public double latitude;

    public Comment(String uid, String userName, String comment, double longitude, double latitude) {
        this.uid = uid;
        this.userName = userName;
        this.comment = comment;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
