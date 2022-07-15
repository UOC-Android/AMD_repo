package com.andresgqjob.mapgame.model;

import androidx.annotation.NonNull;

public class Quiz
{
    public String desc;
    public double lon;
    public double lat;

    public Quiz() {}

    public Quiz(String desc, double lon, double lat) {
        this.desc = desc;
        this.lon = lon;
        this.lat = lat;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @NonNull
    @Override
    public String toString() {
        return "Quiz{" +
                "description='" + desc + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
