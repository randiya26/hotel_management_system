package com.hotel.controller;

import com.hotel.dao.HotelDAO;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class HotelController {

    private final HotelDAO dao;

    public HotelController(final HotelDAO dao) {
        this.dao = Objects.requireNonNull(dao, "dao must not be null");
    }

    // --- Rooms ---
    public Room createRoom(final String type, final double price) {
        if (type == null || type.isBlank()) throw new IllegalArgumentException("type must not be blank");
        if (price < 0d) throw new IllegalArgumentException("price must be >= 0");
        return dao.addRoom(type.trim(), price);
    }

    public List<Room> listRooms() {
        return dao.getAllRooms();
    }

    public void updateRoomAvailability(final int roomId, final boolean available) {
        dao.updateRoomAvailability(roomId, available);
    }

    // --- Customers ---
    public Customer createCustomer(final String name, final String phone, final String nic) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("phone must not be blank");
        if (nic == null || nic.isBlank()) throw new IllegalArgumentException("nic must not be blank");
        return dao.addCustomer(name.trim(), phone.trim(), nic.trim());
    }

    public List<Customer> listCustomers() {
        return dao.getAllCustomers();
    }

    public void updateCustomer(Customer customer) {
        if (customer == null) throw new IllegalArgumentException("customer must not be null");
        dao.updateCustomer(customer);
    }

    // --- Bookings ---
    public Booking createBooking(final int roomId, final int customerId, final LocalDate from, final LocalDate to) {
        if (from == null || to == null) throw new IllegalArgumentException("from/to must not be null");
        if (to.isBefore(from)) throw new IllegalArgumentException("to date must be on/after from date");
        return dao.addBooking(roomId, customerId, from, to);
    }

    public List<Booking> listBookings() {
        return dao.getAllBookings();
    }

    // ✨ NEW: Cancel Booking
    public void deleteBooking(int bookingId) {
        if (bookingId <= 0) throw new IllegalArgumentException("Invalid booking ID");
        dao.deleteBooking(bookingId);
    }

    // --- Login ---
    public boolean login(final String username, final String password) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username must not be blank");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password must not be blank");
        return dao.checkLogin(username, password);
    }
}
