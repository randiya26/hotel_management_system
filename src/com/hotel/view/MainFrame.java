package com.hotel.view;

import com.hotel.controller.HotelController;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final HotelController controller;

    public MainFrame(HotelController controller) {
        this.controller = controller;

        setTitle("Hotel Management System - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Top Header =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(100, 60));

        JLabel lblTitle = new JLabel("Hotel Management System - Dashboard", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        // Logout button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

       btnLogout.addActionListener(e -> {
    try {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame(controller).setVisible(true);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Logout failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});


        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ===== Tabs =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));
        tabs.setBackground(new Color(245, 250, 255));

        tabs.addTab("🏨 Rooms", new RoomPanel(controller));
        tabs.addTab("👤 Customers", new CustomerPanel(controller));
        tabs.addTab("📅 Bookings", new BookingPanel(controller));

        add(tabs, BorderLayout.CENTER);
    }
}
