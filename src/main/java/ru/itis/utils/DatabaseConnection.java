package ru.itis.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/dbservlets1";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "------";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("Error connecting to the database", e);
        }
    }
}
