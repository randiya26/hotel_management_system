package com.hotel;

import com.hotel.dao.HotelDAO;
import com.hotel.controller.HotelController;
import com.hotel.view.MainFrame;
import com.hotel.view.LoginFrame;


import javax.swing.SwingUtilities;

import com.hotel.dao.DBConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Initialize DAO and Controller
        HotelDAO dao = new HotelDAO();
        HotelController controller = new HotelController(dao);

        // Create initial rooms (only if you want to pre-populate)
        controller.createRoom("Single", 5000);
        controller.createRoom("Double", 8000);
        controller.createRoom("Suite", 15000);

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame view = new MainFrame(controller);
            view.setVisible(true);
        });

        
    }
}
