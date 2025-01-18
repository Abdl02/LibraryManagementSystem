package com.library.management.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:~/librarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void initializeDatabase() {
        System.out.println("Database connection initialized.");
    }
}