package com.financemanager.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URl = "jdbc:sqlite:fintrue.db";

    public static void initDatabase(){
        String sql = """
                CREATE TABLE IF NOT EXISTS transactions(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    description TEXT NOT NULL,
                    category TEXT NOT NULL,
                    amount REAL NOT NULL,
                    date TEXT NOT NULL,
                    type TEXT NOT NULL
                )
                """;

        try(Connection con = getConnection(); var stmt = con.createStatement()){
            stmt.execute(sql);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URl);
    }
}
