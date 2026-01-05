package com.financemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinanceManagerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                FinanceManagerApp.class.getResource("/view/fxml/login.fxml")
        );

        Scene scene = new Scene(loader.load(), 400, 300);
        stage.setTitle("Fintrue - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
