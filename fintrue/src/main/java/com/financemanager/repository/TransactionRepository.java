package com.financemanager.repository;

import com.financemanager.domain.Transaction;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void save(Transaction t){
        String sql = " INSERT INTO transactions (description, category, amount, date, type) VALUES(?,?,?,?,?)";

        try(Connection con = DatabaseManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, t.getDescription());
            pstmt.setString(2, t.getCategory());
            pstmt.setDouble(3, t.getAmount());
            pstmt.setString(4, t.getFormattedDate().toString());
            pstmt.setString(5, t.getType());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> findAll(){
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";

        try(Connection con = DatabaseManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                Transaction t = new Transaction(
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        LocalDate.parse(rs.getString("date")),
                        rs.getString("type")
                );
                transactions.add(t);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return transactions;
    }

}
