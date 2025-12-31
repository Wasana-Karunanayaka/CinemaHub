package com.cinemahub.util;

import java.sql.*;

/**
 * Utility class to verify database state and table counts.
 * 
 * @author Wasana Karunanayaka
 */
public class VerifyDB {
    public static void main(String[] args) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            printCount(conn, "movies");
            printCount(conn, "showtimes");
            printCount(conn, "bookings");
            printCount(conn, "booking_seats");

            // Optional: Print details
            System.out.println("\nMovies:");
            printQuery(conn, "SELECT * FROM movies");
            System.out.println("\nShowTimes:");
            printQuery(conn, "SELECT * FROM showtimes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printCount(Connection conn, String table) throws SQLException {
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
            if (rs.next()) {
                System.out.println(table + " count: " + rs.getInt(1));
            }
        }
    }

    private static void printQuery(Connection conn, String query) throws SQLException {
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + ", ");
                }
                System.out.println();
            }
        }
    }
}
