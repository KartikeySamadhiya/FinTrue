package com.financemanager.controller;

import com.financemanager.domain.User;
import com.financemanager.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private static User currentUser;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;

    @FXML
    private void onLoginClicked() throws Exception{
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter both email and password.");
            return;
        }

        User loggedInUser = UserRepository.authenticate(email, password);

        if (loggedInUser != null) {
            currentUser = loggedInUser;
            try {
                FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/view/fxml/dashboard.fxml"));
                Scene dashboardScene = new Scene(dashboardLoader.load(), 950, 620);
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Fintrue - Dashboard (" + loggedInUser.getEmail() + ")");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load dashboard: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid email or password.");
        }
    }


    public void onRegisterClicked(){
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Registration Error", "Email and password cannot be blank.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Registration Error", "Please provide a valid email address.");
            return;
        }
        boolean created = UserRepository.createUser(email, password);
        if (created) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully! You can now log in.");
            passwordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An account with this email already exists or an error occurred.");
        }
    }


    public void showAlert(Alert.AlertType type, String title, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        if (emailField.getScene() != null && emailField.getScene().getWindow() != null) {
            alert.initOwner(emailField.getScene().getWindow());
        }
        alert.showAndWait();
    }


    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user){
        currentUser = user;
    }

}
