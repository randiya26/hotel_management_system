package com.hotel.model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private Room room;
    private Customer customer;
    private LocalDate fromDate;
    private LocalDate toDate;

    public Booking(int id, Room room, Customer customer, LocalDate fromDate, LocalDate toDate) {
        this.id = id;
        this.room = room;
        this.customer = customer;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getId() { return id; }
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }

    @Override
    public String toString() {
        return "Booking #" + id + ": " + room + " -> " + customer + " (" + fromDate + " to " + toDate + ")";
    }
}
