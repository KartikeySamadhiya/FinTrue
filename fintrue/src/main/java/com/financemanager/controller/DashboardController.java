package com.financemanager.controller;
import javafx.fxml.FXML ;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class DashboardController {
    @FXML private Label totalBalanceLabel ;
    @FXML private Label totalExpenseLabel ;
    @FXML private Label totalIncomeLabel;
    @FXML private PieChart expensePieChart;
    @FXML private TableView<?> transactionsTable;
    @FXML private TableColumn<?, ?> dateCol, descCol, categoryCol, amountCol;


    @FXML private void onAddExpense(){};
    @FXML private void onAddIncome(){};
    @FXML private void onLogout(){
        Stage stage = (Stage) totalBalanceLabel.getScene().getWindow();
        stage.close();
    }
}
