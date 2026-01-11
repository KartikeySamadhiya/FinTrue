package com.financemanager.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URl = "jdbc:sqlite:fintrue.db";

    public static void initDatabase(){
        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            )
            """);
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                description TEXT NOT NULL,
                category TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                type TEXT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URl);
    }
}
