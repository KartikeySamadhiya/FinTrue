package com.financemanager.controller;

import com.financemanager.domain.Transaction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddExpenseController {
    @FXML private TextField descField ;
    @FXML private ComboBox<String> categoryCombo ;
    @FXML private TextField amountField;
    @FXML private Button saveButton;

    private DashboardController dashboardController;  // Reference to update charts

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    private void initialize() {
        categoryCombo.setItems(FXCollections.observableArrayList(
                "Food", "Transport", "Shopping", "Bills", "Entertainment"
        ));
    }


    @FXML
    private void onSave() {
        String desc = descField.getText();
        String cat  = categoryCombo.getValue();
        double amount = Double.parseDouble(amountField.getText());

        Transaction expense = new Transaction(desc,cat,amount, LocalDate.now(),"Expense");

        dashboardController.addTransaction(expense);

        // Close modal
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) descField.getScene().getWindow();
        stage.close();
    }

}
