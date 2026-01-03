package com.financemanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void onLoginClicked() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Demo login (replace with your SQL later)
        if ("admin@fintrue.com".equals(email) && "password123".equals(password)) {
            System.out.println("Login successful!");
            // Navigate to dashboard FXML
        } else {
            // Show error (add Alert later)
            System.out.println("Login failed");
        }
    }

}
