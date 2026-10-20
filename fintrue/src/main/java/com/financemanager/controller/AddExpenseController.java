package com.financemanager.controller;

import com.financemanager.domain.Transaction;
import com.financemanager.repository.TransactionRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddExpenseController {
    @FXML private Label titleLabel ;
    @FXML private TextField descField ;
    @FXML private ComboBox<String> categoryCombo ;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;

    private TransactionRepository transactionRepo;
    private DashboardController dashboardController;  // Reference to update charts

    private String transactionType = "EXPENSE";

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    public void setTransactionRepo(TransactionRepository repo) {
        this.transactionRepo = repo;
    }

    public void setTransactionType(String type){
        this.transactionType = type;
        if("INCOME".equalsIgnoreCase(type)){
            titleLabel.setText("Add Income");
            saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
            categoryCombo.setItems(FXCollections.observableArrayList(
                    "Salary", "Freelance", "Investments", "Bonus", "Rental", "Other"
            ));
        } else {
          titleLabel.setText("Add Expense");
          saveButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
          categoryCombo.setItems(FXCollections.observableArrayList(
                  "Food", "Transport", "Shopping", "Bills", "Entertainment", "Health", "Other"
          ));
        }
    }

    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());

        categoryCombo.setItems(FXCollections.observableArrayList(
                "Food", "Transport", "Shopping", "Bills", "Entertainment", "Other"
        ));
    }


    @FXML
    private void onSave() {
        String desc = descField.getText() != null ? descField.getText().trim() : "";
        String cat  = categoryCombo.getValue();
        String amountText = amountField.getText() != null ? amountField.getText().trim() : "";
        LocalDate date = datePicker.getValue();


        if(desc.isEmpty()) {
            showAlert("Validation Error", "Please provide a description.");
            return;
        }
        if(cat == null || cat.isEmpty()){
            showAlert("Validation Error", "Please select a category.");
            return;
        }
        if(date == null){
            showAlert("Validation Error", "Please select a date.");
            return;
        }


        double amount ;
        try {
            amount = Double.parseDouble(amountText);
            if(amount <= 0){
                showAlert("Validation Error", "Amount must be greater than aero.");
                return;
            }
        } catch (NumberFormatException e){
            showAlert("Validation Error", "Please enter a valid numeric amount.");
            return;
        }


        Transaction transaction = new Transaction(desc,cat,amount, date, transactionType);
        long userId = LoginController.getCurrentUser().getId();

        transactionRepo.save(transaction, userId);

        dashboardController.addTransaction(transaction);

        // Close modal
        closeModal();
    }

    @FXML
    private void onCancel() {
        closeModal();
    }


    private void closeModal(){
        Stage stage = (Stage) descField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(descField.getScene().getWindow());
        alert.showAndWait();
    }
}
