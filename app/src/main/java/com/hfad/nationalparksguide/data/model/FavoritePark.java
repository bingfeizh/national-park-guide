package com.hfad.nationalparksguide.data.model;

public class FavoritePark {
    String parkName;

    public FavoritePark(){

    }

    public FavoritePark(String parkName){
        this.parkName = parkName;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }
}
