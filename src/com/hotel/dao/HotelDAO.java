package com.hotel.dao;

import com.hotel.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    // --- Rooms ---
    public Room addRoom(String type, double price) {
        String sql = "INSERT INTO rooms (type, price, available) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, type);
            ps.setDouble(2, price);
            ps.setBoolean(3, true);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return new Room(rs.getInt(1), type, price, true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                rooms.add(new Room(rs.getInt("id"), rs.getString("type"), rs.getDouble("price"), rs.getBoolean("available")));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return rooms;
    }

    public void updateRoomAvailability(int roomId, boolean available) {
        String sql = "UPDATE rooms SET available=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, roomId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- Customers ---
    public Customer addCustomer(String name, String phone, String nic) {
        String sql = "INSERT INTO customers (name, phone, nic) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, nic);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new Customer(id, name, phone, nic);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name=?, phone=?, nic=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getNic());
            ps.setInt(4, customer.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("phone"), rs.getString("nic")));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return customers;
    }

    // --- Bookings ---
    public Booking addBooking(int roomId, int customerId, LocalDate from, LocalDate to) {
        String sql = "INSERT INTO bookings (room_id, customer_id, from_date, to_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, roomId);
            ps.setInt(2, customerId);
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));
            ps.executeUpdate();

            updateRoomAvailability(roomId, false);

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return new Booking(rs.getInt(1), getRoomById(roomId), getCustomerById(customerId), from, to);

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.id as bid, r.id as rid, r.type, r.price, r.available, " +
                     "c.id as cid, c.name, c.phone, c.nic, b.from_date, b.to_date " +
                     "FROM bookings b " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "JOIN customers c ON b.customer_id = c.id";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Room r = new Room(rs.getInt("rid"), rs.getString("type"), rs.getDouble("price"), rs.getBoolean("available"));
                Customer c = new Customer(rs.getInt("cid"), rs.getString("name"), rs.getString("phone"), rs.getString("nic"));
                bookings.add(new Booking(rs.getInt("bid"), r, c, rs.getDate("from_date").toLocalDate(), rs.getDate("to_date").toLocalDate()));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return bookings;
    }

    // ✨ NEW: Cancel Booking
    public void deleteBooking(int bookingId) {
        if (bookingId <= 0) return;

        try (Connection conn = DBConnection.getConnection()) {
            // Get the room_id before deletion to restore availability
            String selectSql = "SELECT room_id FROM bookings WHERE id=?";
            int roomId = -1;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) roomId = rs.getInt("room_id");
            }

            // Delete booking
            String deleteSql = "DELETE FROM bookings WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setInt(1, bookingId);
                ps.executeUpdate();
            }

            // Restore room availability
            if (roomId != -1) updateRoomAvailability(roomId, true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Room getRoomById(int id) {
        for (Room r : getAllRooms()) if (r.getId() == id) return r;
        return null;
    }

    public Customer getCustomerById(int id) {
        for (Customer c : getAllCustomers()) if (c.getId() == id) return c;
        return null;
    }

    // --- Login ---
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
