package com.example.bustracking;

public class BusStop {
    private String stopName;
    private double latitude;
    private double longitude;

    public BusStop(String stopName, double latitude, double longitude) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStopName() {
        return stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}