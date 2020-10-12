package com.hfad.nationalparksguide.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventList {

    @SerializedName("data")
    private ArrayList<Event> eventList;

    public EventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }
}

