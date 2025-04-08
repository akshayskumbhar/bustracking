package com.example.bustracking;

public class Ticket {
    public String studentName, rollNo, start, end, duration;
    public int price;
    public long expiryTime;


    public Ticket(String studentName, String rollNo, String start, String end, String duration, int price, long expiryTime) {
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.price = price;
        this.expiryTime = expiryTime;
    }
}
