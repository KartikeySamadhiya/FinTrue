package com.financemanager.controller;

import com.financemanager.domain.User;
import com.financemanager.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private static User currentUser;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML private Button registerButton ;

    @FXML
    private void onLoginClicked() throws Exception{
        String email = emailField.getText();
        String password = passwordField.getText();

        User loggedInUser = UserRepository.authenticate(email, password);

        if (loggedInUser != null) {
            currentUser = loggedInUser;
            System.out.println("Login successful!");
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/view/fxml/dashboard.fxml"));
            Scene dashboardScene = new Scene(dashboardLoader.load(), 900, 600);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Fintrue - Dashboard");
        } else {
            System.out.println("Login failed");
        }
    }

    public void onRegisterClicked(){
        String email = emailField.getText();
        String password = passwordField.getText();

        UserRepository.createUser(email,password);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

}
