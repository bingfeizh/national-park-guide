package com.hfad.nationalparksguide.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ParkInfo {
    @PrimaryKey
    @NonNull
    public String uid;

    @ColumnInfo(name = "park_name")
    public String parkName;

    @ColumnInfo(name = "park_description")
    public String parkDescription;

    @ColumnInfo(name = "park_image")
    public String parkImage;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "parkCode")
    public String parkCode;

    public ParkInfo(String uid, String parkName, String parkDescription, String parkImage, String url, String parkCode) {
        this.uid = uid;
        this.parkName = parkName;
        this.parkDescription = parkDescription;
        this.parkImage = parkImage;
        this.url = url;
        this.parkCode = parkCode;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public String getParkName() {
        return parkName;
    }

    public String getParkDescription() {
        return parkDescription;
    }
}
