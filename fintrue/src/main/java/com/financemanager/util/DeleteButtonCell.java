package com.financemanager.util;

import com.financemanager.domain.Transaction;
import com.financemanager.repository.TransactionRepository;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;

public class DeleteButtonCell extends TableCell<Transaction, Void> {
    private final ObservableList<Transaction> transactions;
    private final TransactionRepository repo;

    public DeleteButtonCell(ObservableList<Transaction> transactions, TransactionRepository repo) {
        this.transactions = transactions;
        this.repo = repo;
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            Button deleteBtn = new Button("❌");
            deleteBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-pref-width: 40px;");

            deleteBtn.setOnAction(e -> {
                Transaction transaction = getTableRow().getItem();
                if (transaction == null) return;

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Transaction");
                alert.setHeaderText("Are you sure?");
                alert.setContentText(transaction.getDescription() + " - ₹" + transaction.getAmount());
                alert.initOwner((Stage) getScene().getWindow());

                if (alert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
                    repo.delete(transaction.getId());
                    transactions.remove(transaction);
                    System.out.println("🗑️ Deleted: " + transaction.getDescription());
                }
            });

            setGraphic(deleteBtn);
        }
    }
}
