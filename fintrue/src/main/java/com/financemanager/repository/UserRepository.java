package com.financemanager.repository;

import com.financemanager.domain.User;
import java.sql.*;

public class UserRepository {
    public static User authenticate(String email, String password){
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try(Connection con = DatabaseManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
        ){
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
            }
        }catch (SQLException e){
            System.err.println("Auth error: " + e.getMessage());
        }

        return null ;
    }

    public static boolean createUser(String email, String password){
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)" ;
        try (Connection conn = DatabaseManager.getConnection() ;
              PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true ;
        }catch (SQLException e){
            System.err.println("Register error: " + e.getMessage());
            return false ;
        }
    }

}