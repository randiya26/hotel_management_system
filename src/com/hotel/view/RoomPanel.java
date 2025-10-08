package com.hotel.view;

import com.hotel.controller.HotelController;
import com.hotel.model.Booking;
import com.hotel.model.Room;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomPanel extends JPanel {

    private final HotelController controller;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> roomList = new JList<>(listModel);

    // Map to track which booking ID belongs to each list item (kept for info use)
    private final Map<String, Integer> bookingMap = new HashMap<>();

    public RoomPanel(HotelController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 250, 255));

        // ===== Title =====
        JLabel lblTitle = new JLabel("Room Management", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Left: Room form & filter =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtType = new JTextField(15);
        JTextField txtPrice = new JTextField(12);
        JTextField txtFilter = new JTextField(10);

        // Room Type & Price
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtType, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPrice, gbc);

        // Filter
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Filter Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtFilter, gbc);

        // Buttons
        JButton btnAdd = new JButton("Add Room");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnFilter = new JButton("Filter");

        btnAdd.setBackground(new Color(0, 153, 51));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnRefresh.setBackground(new Color(0, 102, 204));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);

        btnFilter.setBackground(new Color(255, 153, 0));
        btnFilter.setForeground(Color.WHITE);
        btnFilter.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnFilter);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ===== Right: Room list =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Rooms & Availability"));

        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setFont(new Font("Arial", Font.PLAIN, 14));
        roomList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setOpaque(true);
            if (value.contains("BOOKED")) label.setForeground(Color.RED);
            else label.setForeground(Color.BLACK);
            if (isSelected) {
                label.setBackground(new Color(0, 102, 204));
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(Color.WHITE);
            }
            return label;
        });

        rightPanel.add(new JScrollPane(roomList), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);

        // ===== Actions =====
        btnAdd.addActionListener(e -> {
            try {
                String type = txtType.getText().trim();
                if (type.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Room type is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                double price;
                try {
                    price = Double.parseDouble(txtPrice.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                controller.createRoom(type, price);
                txtType.setText("");
                txtPrice.setText("");
                refresh(txtFilter.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to add room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRefresh.addActionListener(e -> refresh(txtFilter.getText().trim()));
        btnFilter.addActionListener(e -> refresh(txtFilter.getText().trim()));

        // ===== Initial load =====
        refresh("");
    }

    private void refresh() { refresh(""); }

    private void refresh(String typeFilter) {
        try {
            listModel.clear();
            bookingMap.clear();  // reset the map

            List<Room> rooms = controller.listRooms();
            List<Booking> bookings = controller.listBookings();
            LocalDate today = LocalDate.now();

            for (Room r : rooms) {
                if (!typeFilter.isEmpty() && !r.getType().toLowerCase().contains(typeFilter.toLowerCase()))
                    continue;

                String display = r.toString();

                // Check if room is booked today
                Integer activeBookingId = findActiveBookingIdForRoom(r, bookings, today);
                if (activeBookingId != null) {
                    display += " — BOOKED (Booking #" + activeBookingId + ")";
                    bookingMap.put(display, activeBookingId); // map entry to booking ID
                } else if (!r.isAvailable()) {
                    display += " — BOOKED";
                }

                listModel.addElement(display);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load rooms: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer findActiveBookingIdForRoom(Room room, List<Booking> bookings, LocalDate today) {
        for (Booking b : bookings) {
            Room bookedRoom = b.getRoom();
            if (bookedRoom != null && bookedRoom.getId() == room.getId()) {
                LocalDate from = b.getFromDate();
                LocalDate to = b.getToDate();
                if ((from.isBefore(today) || from.isEqual(today)) &&
                    (to.isAfter(today) || to.isEqual(today))) {
                    return b.getId();
                }
            }
        }
        return null;
    }
}
