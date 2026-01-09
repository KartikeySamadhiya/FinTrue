package com.financemanager.controller;
import com.financemanager.domain.Transaction;
import com.financemanager.repository.DatabaseManager;
import com.financemanager.repository.TransactionRepository;
import javafx.collections.FXCollections;
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
import java.util.List;

public class DashboardController {
    @FXML private Label totalBalanceLabel ;
    @FXML private Label totalExpensesLabel ;
    @FXML private Label totalIncomeLabel;
    @FXML private PieChart expensePieChart;
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<?, ?> dateCol, descCol, categoryCol, amountCol;

    private List<Transaction> transactions = new ArrayList<>();
    private TransactionRepository transactionRepo = new TransactionRepository();

    @FXML private void onAddExpense() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/add_expense.fxml"));
        Parent root = loader.load();

        AddExpenseController controller = loader.getController();
        controller.setDashboardController(this);

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

        loadTransactions();

        expensePieChart.getData().addAll(
                new PieChart.Data("Food", 300),
                new PieChart.Data("Transport", 150),
                new PieChart.Data("Shopping", 95)
        );
        totalBalanceLabel.setText("$1,450");
        totalExpensesLabel.setText("Expenses: $545");
        totalIncomeLabel.setText("Income: $2,000");

        transactions.add(new Transaction("Lunch", "Food", 15.50, LocalDate.now(), "EXPENSE"));
        transactions.add(new Transaction("Bus", "Transport", 2.00, LocalDate.now().minusDays(1), "EXPENSE"));

        transactionsTable.setItems(FXCollections.observableArrayList(transactions));
    }


    private void loadTransactions() {
        transactions.clear();
        transactions.addAll(transactionRepo.findAll());
        transactionsTable.setItems(FXCollections.observableArrayList(transactions));
    }

}
