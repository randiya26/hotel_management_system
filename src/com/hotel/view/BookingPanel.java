package com.hotel.view;

import com.hotel.controller.HotelController;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

// ==== JDBC ====
import java.sql.Connection;
import java.sql.DriverManager;

// ==== JasperReports ====
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class BookingPanel extends JPanel {

    private final HotelController controller;

    private final JComboBox<Customer> comboCustomer = new JComboBox<>();
    private final JComboBox<Room> comboRoom = new JComboBox<>();
    private final JTextField txtFrom = new JTextField(10);
    private final JTextField txtTo = new JTextField(10);
    private final JButton btnBook = new JButton("Book Room");
    private final JButton btnRefresh = new JButton("Refresh");
    private final JButton btnPrint = new JButton("Print");

    // Panel to show booking details
    private final JPanel bookingDetailsPanel = new JPanel();
    private final JScrollPane scrollPane;

    // ===== DB config =====
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_URL    = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER   = "root";
    private static final String JDBC_PASS   = ""; // set if you have a password

    // ===== Report path =====
    private static final String REPORT_JASPER_PATH =
            "C:\\Users\\MSI\\Documents\\NetBeansProjects\\HotelManagementSystem\\booking.jasper";

    public BookingPanel(HotelController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 250, 255));

        // ===== Title =====
        JLabel lblTitle = new JLabel("Room Booking", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Left: Booking form =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboCustomer, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboRoom, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("From (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtFrom, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("To (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTo, gbc);

        // Buttons row
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        styleBtnPrimary(btnBook);
        styleBtnSecondary(btnRefresh);
        styleBtnDark(btnPrint);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnBook);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnPrint);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // ===== Right: Booking Details Panel =====
        bookingDetailsPanel.setLayout(new BoxLayout(bookingDetailsPanel, BoxLayout.Y_AXIS));
        bookingDetailsPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(bookingDetailsPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Actions =====
        btnBook.addActionListener(e -> onBook());
        btnRefresh.addActionListener(e -> refresh());
        btnPrint.addActionListener(e -> printBookings());

        // Defaults
        txtFrom.setText(LocalDate.now().toString());
        txtTo.setText(LocalDate.now().plusDays(1).toString());

        refresh();
    }

    private void onBook() {
        try {
            Customer c = (Customer) comboCustomer.getSelectedItem();
            Room r = (Room) comboRoom.getSelectedItem();

            LocalDate from, to;
            try {
                from = LocalDate.parse(txtFrom.getText().trim());
                to = LocalDate.parse(txtTo.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dates must be in yyyy-mm-dd format.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (c == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Please select a room.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!to.isAfter(from)) {
                JOptionPane.showMessageDialog(this, "‘To’ date must be after ‘From’ date.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!r.isAvailable()) {
                JOptionPane.showMessageDialog(this, "Selected room is not available.", "Availability", JOptionPane.WARNING_MESSAGE);
                refresh();
                return;
            }

            controller.createBooking(r.getId(), c.getId(), from, to);
            controller.updateRoomAvailability(r.getId(), false);

            refresh();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to create booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refresh() {
        try {
            comboCustomer.removeAllItems();
            for (Customer c : controller.listCustomers()) comboCustomer.addItem(c);

            comboRoom.removeAllItems();
            for (Room r : controller.listRooms()) if (r.isAvailable()) comboRoom.addItem(r);

            bookingDetailsPanel.removeAll();
            List<Booking> bookings = controller.listBookings();
            for (Booking b : bookings) {
                addBookingDetail(b);
            }

            bookingDetailsPanel.revalidate();
            bookingDetailsPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to refresh bookings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === Add booking detail with Cancel button ===
    private void addBookingDetail(Booking b) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel lbl = new JLabel("<html>"
                + "<b>Customer:</b> " + b.getCustomer().getName() + " | "
                + "<b>Room:</b> " + b.getRoom() + " | "
                + "<b>From:</b> " + b.getFromDate() + " | "
                + "<b>To:</b> " + b.getToDate()
                + "</html>");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        btnCancel.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Cancel this booking?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteBooking(b.getId());
                    controller.updateRoomAvailability(b.getRoom().getId(), true);
                    JOptionPane.showMessageDialog(this, "Booking canceled.");
                    refresh();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to cancel booking: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        row.add(lbl, BorderLayout.CENTER);
        row.add(btnCancel, BorderLayout.EAST);

        bookingDetailsPanel.add(row);
    }

    // ===== Styling helpers =====
    private void styleBtnPrimary(JButton b) { b.setBackground(new Color(0,102,204)); b.setForeground(Color.WHITE); b.setFocusPainted(false); }
    private void styleBtnSecondary(JButton b) { b.setBackground(new Color(100,149,237)); b.setForeground(Color.WHITE); b.setFocusPainted(false); }
    private void styleBtnDark(JButton b) { b.setBackground(new Color(70,70,70)); b.setForeground(Color.WHITE); b.setFocusPainted(false); }

    // =========================
    // PRINT (viewer only)
    // =========================
    private void printBookings() {
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                JasperPrint jp = JasperFillManager.fillReport(REPORT_JASPER_PATH, null, con);
                JasperViewer viewer = new JasperViewer(jp, false);
                viewer.setTitle("Bookings Report");
                viewer.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
