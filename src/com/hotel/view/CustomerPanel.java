package com.hotel.view;

import com.hotel.controller.HotelController;
import com.hotel.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

// JasperReports
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class CustomerPanel extends JPanel {

    private final HotelController controller;

    private final JTextField txtName  = new JTextField(15);
    private final JTextField txtPhone = new JTextField(12);
    private final JTextField txtNic   = new JTextField(15);

    private final JPanel cardsPanel = new JPanel();
    private final JScrollPane cardsScrollPane;

    // Keep track of selected customer for update
    private Customer selectedCustomer = null;

    // ====== CONFIG (edit these for your environment) ======
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_URL    = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER   = "root";
    private static final String JDBC_PASS   = "";   // set if you have a password

    // Use the compiled .jasper (export it in Jaspersoft Studio: Right click → Compile Report)
    private static final String REPORT_JASPER_PATH =
            "C:\\Users\\MSI\\Documents\\NetBeansProjects\\HotelManagementSystem\\customers.jasper";
    // ======================================================

    public CustomerPanel(HotelController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 250, 255));

        // ===== Title =====
        JLabel lblTitle = new JLabel("Customer Management", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Left: Form =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("NIC:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNic, gbc);

        // ===== Buttons =====
        JButton btnAdd     = new JButton("Add Customer");
        JButton btnUpdate  = new JButton("Update Customer");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnPrint   = new JButton("Print");

        btnAdd.setBackground(new Color(0, 153, 51));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnUpdate.setBackground(new Color(255, 140, 0));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);

        btnRefresh.setBackground(new Color(0, 102, 204));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);

        btnPrint.setBackground(new Color(70, 70, 70));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnPrint);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ===== Right: Customer cards panel =====
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(Color.WHITE);

        cardsScrollPane = new JScrollPane(cardsPanel);
        cardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cardsScrollPane.setBorder(BorderFactory.createTitledBorder("Customers"));
        add(cardsScrollPane, BorderLayout.CENTER);

        // ===== Actions =====
        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnRefresh.addActionListener(e -> refresh());
        btnPrint.addActionListener(e -> printCustomers());  // show only report window

        // Initial load
        refresh();
    }

    private void addCustomer() {
        try {
            String name  = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String nic   = txtNic.getText().trim();

            if (!validateInput(name, phone, nic)) return;

            Customer c = controller.createCustomer(name, phone, nic);
            addCustomerCard(c);
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to add customer: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a customer first using the 'Select' button.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String name  = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String nic   = txtNic.getText().trim();

            if (!validateInput(name, phone, nic)) return;

            // Update via controller
            selectedCustomer.setName(name);
            selectedCustomer.setPhone(phone);
            selectedCustomer.setNic(nic);

            controller.updateCustomer(selectedCustomer);  // must be implemented in controller/DAO

            JOptionPane.showMessageDialog(this,
                    "Customer updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            selectedCustomer = null;
            refresh(); // reload cards
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to update customer: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput(String name, String phone, String nic) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (phone.isEmpty() || !phone.matches("\\+?\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Phone must be 7–15 digits (optionally starting with +).",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (nic.isEmpty() || !(nic.matches("\\d{9}[VvXx]") || nic.matches("\\d{12}"))) {
            JOptionPane.showMessageDialog(this,
                    "NIC must be 9 digits + V/X (e.g., 123456789V) or 12 digits.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void refresh() {
        try {
            cardsPanel.removeAll();
            List<Customer> customers = controller.listCustomers();
            for (Customer c : customers) addCustomerCard(c);
            cardsPanel.revalidate();
            cardsPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPhone.setText("");
        txtNic.setText("");
    }

    private void addCustomerCard(Customer c) {
        JPanel card = new JPanel(new GridLayout(4, 1));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(new Color(230, 240, 255));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel lblName  = new JLabel("Name: " + c.getName());
        JLabel lblPhone = new JLabel("Phone: " + c.getPhone());
        JLabel lblNic   = new JLabel("NIC: " + c.getNic());

        JButton btnSelect = new JButton("Select");
        btnSelect.addActionListener(e -> loadCustomerToForm(c));

        card.add(lblName);
        card.add(lblPhone);
        card.add(lblNic);
        card.add(btnSelect);

        cardsPanel.add(card);
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void loadCustomerToForm(Customer c) {
        selectedCustomer = c;
        txtName.setText(c.getName());
        txtPhone.setText(c.getPhone());
        txtNic.setText(c.getNic());
    }

    // =========================
    // PRINT (viewer only)
    // =========================
    private void printCustomers() {
        try {
            // 1) Load JDBC driver
            Class.forName(JDBC_DRIVER);

            // 2) Connect DB (auto-close)
            try (Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {

                // 3) Fill report directly from compiled .jasper
                JasperPrint jp = JasperFillManager.fillReport(REPORT_JASPER_PATH, null, con);

                // 4) Show ONLY the report window
                JasperViewer viewer = new JasperViewer(jp, false);
                viewer.setTitle("Customer Report");
                viewer.setVisible(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
