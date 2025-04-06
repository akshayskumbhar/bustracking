package com.example.bustracking;

public class TransportOption {
    private String time, duration, transportMode, price;

    public TransportOption(String time, String duration, String transportMode, String price) {
        this.time = time;
        this.duration = duration;
        this.transportMode = transportMode;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public String getPrice() {
        return price;
    }
}
