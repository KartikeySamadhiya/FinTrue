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
    private final Runnable onDeleteCallback;
    private final Button deleteBtn;

    public DeleteButtonCell(ObservableList<Transaction> transactions,
                            TransactionRepository repo,
                            Runnable onDeleteCallback) {
        this.transactions = transactions;
        this.repo = repo;
        this.onDeleteCallback = onDeleteCallback;

        this.deleteBtn = new Button("✕");
        this.deleteBtn.setStyle("-fx-background-color: #ff5252; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 3 8 3 8;");
        this.deleteBtn.setOnAction(e -> handleDelete());
    }


    public void handleDelete(){
        Transaction transaction = getTableRow() != null ? getTableRow().getItem() : null;
        if(transaction == null || transaction.getId() == null)  return ;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Delete this record?");
        alert.setContentText(transaction.getDescription() + " - $" + String.format("%.2f",
                transaction.getAmount()));

        if(getScene() != null && getScene().getWindow() != null){
            alert.initOwner((Stage) getScene().getWindow());
        }

        if(alert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
            repo.delete(transaction.getId());
            transactions.remove(transaction);
            if (onDeleteCallback != null){
                onDeleteCallback.run();
            }
        }

    }


    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            setGraphic(deleteBtn);
        }
    }
}
