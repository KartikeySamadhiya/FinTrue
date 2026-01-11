package com.financemanager.controller;
import com.financemanager.domain.Transaction;
import com.financemanager.repository.DatabaseManager;
import com.financemanager.repository.TransactionRepository;
import com.financemanager.util.DeleteButtonCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML ;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {
    @FXML private Label totalBalanceLabel ;
    @FXML private Label totalExpensesLabel ;
    @FXML private Label totalIncomeLabel;
    @FXML private PieChart expensePieChart;
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<?, ?> dateCol, descCol, categoryCol, amountCol;

    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private TransactionRepository transactionRepo = new TransactionRepository();

    @FXML private void onAddExpense() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/add_expense.fxml"));
        Parent root = loader.load();

        AddExpenseController controller = loader.getController();
        controller.setDashboardController(this);
        controller.setTransactionRepo(transactionRepo);

        Stage modal = new Stage();
        modal.setScene(new Scene(root, 350, 300));
        modal.setTitle("Add Expense");
        modal.initModality(Modality.WINDOW_MODAL);  // Blocks dashboard
        modal.initOwner(totalBalanceLabel.getScene().getWindow());
        modal.showAndWait();
    };

    @FXML private void onAddIncome(){};

    public void addTransaction(Transaction t){
        boolean found = false ;
        for(PieChart.Data slice  : expensePieChart.getData()){
            if(slice.getName().equals(t.getCategory())){
                slice.setPieValue(slice.getPieValue() + t.getAmount());
                found = true ;
                break;
            }
        }
        if(!found){
            expensePieChart.getData().add(new PieChart.Data(t.getCategory(), t.getAmount()));
        }

        transactions.add(t);
        transactionsTable.setItems(FXCollections.observableArrayList(transactions));
        updatePieChart();
    }

    @FXML private void onLogout(){
        Stage stage = (Stage) totalBalanceLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {

        DatabaseManager.initDatabase();

        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, Void> deleteCol = new TableColumn<>(" ");
        deleteCol.setCellFactory(param -> new DeleteButtonCell(transactions, transactionRepo));
        transactionsTable.getColumns().add(deleteCol);

        loadTransactions();

        totalBalanceLabel.setText("$0");
        totalExpensesLabel.setText("Expenses: $0");
        totalIncomeLabel.setText("Income: $0");

        transactionsTable.setItems(transactions);

        updatePieChart();
    }


    private void loadTransactions() {
        long currentUserId = LoginController.getCurrentUser().getId();
        transactions.clear();
        List<Transaction> allTransactions = transactionRepo.findByUserId(currentUserId);
        for (Transaction t : allTransactions) {
            t.setId(t.getId());
            transactions.add(t);
        }
    }

    private void updatePieChart() {
        expensePieChart.getData().clear();
        Map<String, Double> categoryTotals = new HashMap<>();

        System.out.println("Total transactions: " + transactions.size());

        for (Transaction t : transactions) {
            String type = t.getType();

            if ("EXPENSE".equals(type)) {
                categoryTotals.merge(t.getCategory(), t.getAmount(), Double::sum);
                System.out.println("✅ Added " + t.getCategory() + ": " + t.getAmount());
            }
        }

        System.out.println("Categories found: " + categoryTotals.size());

        if (categoryTotals.isEmpty()) {
            expensePieChart.getData().add(new PieChart.Data("No Expenses", 100));
        } else {
            categoryTotals.forEach((cat, amt) -> {
                expensePieChart.getData().add(new PieChart.Data(cat, amt));
            });
        }
    }


}
