package com.financemanager.repository;

import com.financemanager.domain.Transaction;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void save(Transaction t, long userId){
        String sql = " INSERT INTO transactions (user_id, description, category, amount, date, type) VALUES(?,?,?,?,?,?)";

        try(Connection con = DatabaseManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, userId);
            pstmt.setString(2, t.getDescription());
            pstmt.setString(3, t.getCategory());
            pstmt.setDouble(4, t.getAmount());
            pstmt.setString(5, t.getFormattedDate());
            pstmt.setString(6, t.getType());
            pstmt.executeUpdate();


            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    t.setId(generatedKeys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving transaction " + e.getMessage(), e);
        }
    }


    public void delete(long id){
        String sql = "DELETE FROM transactions WHERE id = ?" ;
        try(Connection con = DatabaseManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setLong(1,id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("Delete error: " + e.getMessage());
        }
    }


//    public List<Transaction> findAll(){
//        List<Transaction> transactions = new ArrayList<>();
//        String sql = "SELECT * FROM transactions ORDER BY date DESC";
//
//        try(Connection con = DatabaseManager.getConnection();
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(sql)){
//
//            while (rs.next()){
//                Transaction t = new Transaction(
//                        rs.getString("description"),
//                        rs.getString("category"),
//                        rs.getDouble("amount"),
//                        LocalDate.parse(rs.getString("date")),
//                        rs.getString("type")
//                );
//                t.setId(rs.getLong("id"));
//                transactions.add(t);
//            }
//        }catch (SQLException e){
//            throw new RuntimeException(e);
//        }
//
//        return transactions;
//    }

    public List<Transaction> findByUserId(long userId){
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, description, category, amount, date, type FROM transactions WHERE user_id = ? ORDER BY date DESC, id DESC";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Transaction t = new Transaction(
                        rs.getLong("id"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        LocalDate.parse(rs.getString("date")),
                        rs.getString("type")
                );
                transactions.add(t);
            }
        }catch (SQLException e){
            System.err.println("Load error: " + e.getMessage());
        }
        return transactions ;
    }

}
