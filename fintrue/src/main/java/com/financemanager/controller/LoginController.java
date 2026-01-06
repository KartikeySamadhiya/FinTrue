package com.financemanager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void onLoginClicked() throws Exception{
        String email = emailField.getText();
        String password = passwordField.getText();

        // Demo login (replace with your SQL later)
        if ("admin@fintrue.com".equals(email) && "password123".equals(password)) {
            System.out.println("Login successful!");
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/view/fxml/dashboard.fxml"));
            Scene dashboardScene = new Scene(dashboardLoader.load(), 900, 600);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Fintrue - Dashboard");
        } else {
            // Show error (add Alert later)
            System.out.println("Login failed");
        }
    }

}
