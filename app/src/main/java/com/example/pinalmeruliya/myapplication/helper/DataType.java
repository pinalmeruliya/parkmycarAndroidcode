package com.example.pinalmeruliya.myapplication.helper;

import java.util.List;



public class DataType {

    public String description;
    public String place_id;

    List<UserData> geometry;

    public List<UserData> getGeometry() {
        return geometry;
    }

    public void setGeometry(List<UserData> geometry) {
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

