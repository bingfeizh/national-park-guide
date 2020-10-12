package com.hfad.nationalparksguide.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Park {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("designation")
    private String designation;
    @SerializedName("images")
    private List<Image> imageList;
    @SerializedName("url")
    private String url;
    @SerializedName("parkCode")
    private String parkCode;


    public Park(String id, String name, String description, String designation, List<Image> imageList, String url, String parkCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.designation = designation;
        this.imageList = imageList;
        this.url = url;
        this.parkCode = parkCode;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public String getImageURL() {
        return imageList.get(0).getURL();
    }

    public String getURL() {
        return url;
    }

    public String getParkCode() {
        return parkCode;
    }

}

