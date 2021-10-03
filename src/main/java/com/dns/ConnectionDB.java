package com.dns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB {

    private ConnectionDB() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "/database.db");
        String query = "CREATE TABLE IF NOT EXISTS list" + "(url TEXT PRIMARY KEY);";
        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
        }
        return conn;
    }

}
