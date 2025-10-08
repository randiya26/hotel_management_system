package com.hotel.view;

import com.hotel.controller.HotelController;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private HotelController controller;

    public LoginFrame(HotelController controller) {
        this.controller = controller;

        setTitle("Hotel Management System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main background panel
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(240, 248, 255)); // Light background

        // Login panel (center box)
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        loginPanel.setPreferredSize(new Dimension(400, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Hotel Management System", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));

        JLabel lblUser = new JLabel("Username:");
        JTextField txtUser = new JTextField(15);

        JLabel lblPass = new JLabel("Password:");
        JPasswordField txtPass = new JPasswordField(15);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);

        // Add components
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        loginPanel.add(lblUser, gbc);
        gbc.gridx = 1;
        loginPanel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy++;
        loginPanel.add(lblPass, gbc);
        gbc.gridx = 1;
        loginPanel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        loginPanel.add(btnLogin, gbc);

        // Add login panel to background
        backgroundPanel.add(loginPanel);

        add(backgroundPanel);

        // Action listener for login
        btnLogin.addActionListener(e -> {
    try {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (controller.login(user, pass)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new MainFrame(controller).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});

    }
}
