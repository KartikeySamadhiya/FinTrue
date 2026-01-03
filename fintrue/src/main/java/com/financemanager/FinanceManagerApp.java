package com.financemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinanceManagerApp extends Application {  // Class name matches file name
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                FinanceManagerApp.class.getResource("/view/fxml/login.fxml")
        );
        Scene scene = new Scene(loader.load(), 900, 600);
        stage.setTitle("Fintrue - Personal Finance Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
