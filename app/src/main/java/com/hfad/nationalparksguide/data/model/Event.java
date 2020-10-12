package com.hfad.nationalparksguide.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Event {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    public Event(String title, String description) {
        this.title = title;
        this.title = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}


