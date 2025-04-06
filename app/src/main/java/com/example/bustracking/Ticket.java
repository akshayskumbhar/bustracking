package com.example.bustracking;

public class Ticket {
    public String start, end, duration;
    public int price;
    public long expiryTime;

    public Ticket() {
        // Needed for Firebase
    }

    public Ticket(String start, String end, String duration, int price, long expiryTime) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.price = price;
        this.expiryTime = expiryTime;
    }
}
