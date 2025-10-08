package com.hotel.model;

public class Room {
    private int id;
    private String type;
    private double price;
    private boolean available;

    public Room(int id, String type, double price, boolean available) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return type + " (ID:" + id + ") - Rs " + price + " - " + (available ? "Available" : "Booked");
    }
}
