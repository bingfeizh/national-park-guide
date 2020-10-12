package com.hfad.nationalparksguide.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ParkList {

    @SerializedName("data")
    private ArrayList<Park> parkList;

    public ParkList(ArrayList<Park> parkList) {
        this.parkList = parkList;
    }

    public ArrayList<Park> getParkList() {
        return parkList;
    }
}
