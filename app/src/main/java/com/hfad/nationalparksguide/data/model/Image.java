package com.hfad.nationalparksguide.data.model;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("url")
    private String url;


    public Image(String url) {
        this.url = url;
    }


    public String getURL() {
        return url;
    }
}
