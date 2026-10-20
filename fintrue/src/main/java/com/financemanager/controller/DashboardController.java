package com.financemanager.controller;

import com.financemanager.domain.Transaction;
import com.financemanager.repository.DatabaseManager;
import com.financemanager.repository.TransactionRepository;
import com.financemanager.util.DeleteButtonCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML ;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {
    @FXML
    private Label totalBalanceLabel;
    @FXML
    private Label totalExpensesLabel;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private PieChart expensePieChart;
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<?, ?> dateCol, descCol, categoryCol, amountCol;

    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private TransactionRepository transactionRepo = new TransactionRepository();


    @FXML
    private void initialize() {

        DatabaseManager.initDatabase();

        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, Void> deleteCol = new TableColumn<>(" ");
        deleteCol.setCellFactory(param -> new DeleteButtonCell(transactions, transactionRepo, this::refreshDashboard));
        deleteCol.setPrefWidth(60);
        transactionsTable.getColumns().add(deleteCol);


        transactionsTable.setItems(transactions);

        loadTransactions();
        refreshDashboard();
    }


    private void loadTransactions() {
        transactions.clear();
        if (LoginController.getCurrentUser() != null) {
            long currentUserId = LoginController.getCurrentUser().getId();
            List<Transaction> userTransactions = transactionRepo.findByUserId(currentUserId);
            transactions.addAll(userTransactions);
        }
    }


    public void addTransaction(Transaction t) {
        transactions.add(0, t);
        refreshDashboard();
    }


    public void refreshDashboard() {
        updateSummaryCards();
        updatePieChart();
    }


    private void updateSummaryCards() {
        double totalIncome = 0.0;
        double totalExpense = 0.0;

        for (Transaction t : transactions) {
            if ("INCOME".equalsIgnoreCase(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("EXPENSE".equalsIgnoreCase(t.getType())) {
                totalExpense += t.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        totalBalanceLabel.setText(String.format("Net balance: $%.2f", balance));
        totalIncomeLabel.setText(String.format("Income: $%.2f", totalIncome));
        totalExpensesLabel.setText(String.format("Expenses: $%.2f", totalExpense));

        if (balance < 0) {
            totalBalanceLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e53935;"); //red
        } else {
            totalBalanceLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        }
    }


    private void updatePieChart() {
        expensePieChart.getData().clear();
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Transaction t : transactions) {
            String type = t.getType();

            if ("EXPENSE".equals(t.getType())) {
                categoryTotals.merge(t.getCategory(), t.getAmount(), Double::sum);
                //System.out.println("Added " + t.getCategory() + ": " + t.getAmount());
            }
        }

        //System.out.println("Categories found: " + categoryTotals.size());

        if (categoryTotals.isEmpty()) {
            expensePieChart.getData().add(new PieChart.Data("No Expenses", 1));
        } else {
            categoryTotals.forEach((cat, amt) ->
                    expensePieChart.getData().add(new PieChart.Data(cat + " ($" + String.format("%.0f", amt) + ")", amt))
            );
        }
    }


    @FXML
    private void onAddExpense() throws IOException {
        openTransactionModal("EXPENSE");
    }

    ;

    @FXML
    private void onAddIncome() throws IOException {
        openTransactionModal("INCOME");
    }


    private void openTransactionModal(String type) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/add_expense.fxml"));
        Parent root = loader.load();

        AddExpenseController controller = loader.getController();
        controller.setDashboardController(this);
        controller.setTransactionRepo(transactionRepo);
        controller.setTransactionType(type);

        Stage modal = new Stage();
        modal.setScene(new Scene(root, 300, 360));
        modal.setTitle("Add " + ("INCOME".equals(type) ? "Income" : "Expense"));
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(totalBalanceLabel.getScene().getWindow());
        modal.showAndWait();
    }


    @FXML
    private void onLogout() {
        try {
            LoginController.setCurrentUser(null);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 400, 320);
            Stage stage = (Stage) totalBalanceLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Fintrue - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
