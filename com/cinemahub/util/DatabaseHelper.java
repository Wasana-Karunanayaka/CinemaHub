package com.cinemahub.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the database connection logic.
 * 
 * @author Wasana Karunanayaka
 */
public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/cinemahub";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; // Leave empty or change as per environment

    private static Connection connection;

    private DatabaseHelper() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC Driver (Optional for newer generic JDBC, but good practice
                // for legacy apps)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }
}
