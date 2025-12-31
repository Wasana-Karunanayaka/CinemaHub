package com.cinemahub.dao;

import com.cinemahub.model.Booking;
import com.cinemahub.model.Seat;
import com.cinemahub.model.User;
import com.cinemahub.util.DatabaseHelper;

import java.sql.*;
import java.util.List;

/**
 * Implementation of BookingDAO using JDBC.
 * 
 * @author Wasana Karunanayaka
 */
public class BookingDAOImpl implements BookingDAO {

    /**
     * Saves a booking, creates/verifies the user, and records booked seats
     * transactionally.
     */
    @Override
    public void saveBooking(Booking booking) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. Get or Create User
            // Ideally we check if user exists, but for this simple app, let's just
            // insert/ignore or find.
            // Simplified logic: Check by NIC, if not exists insert, else get ID.
            int userId = getOrCreateUser(booking.getUser(), conn);

            // 2. Insert Booking
            String insertBooking = "INSERT INTO bookings (user_id, showtime_id, total_price) VALUES (?, ?, ?)";
            int bookingId = -1;

            try (PreparedStatement pstmt = conn.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, booking.getShowTime().getId());
                pstmt.setDouble(3, booking.getTotalPrice());
                pstmt.executeUpdate();

                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        bookingId = keys.getInt(1);
                    }
                }
            }

            // 3. Insert Booking Seats
            if (bookingId != -1) {
                String insertSeat = "INSERT INTO booking_seats (booking_id, seat_index, seat_type, price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSeat)) {
                    List<Seat> allSeats = booking.getShowTime().getSeats();
                    List<Seat> bookedSeats = booking.getSeats();

                    for (Seat seat : bookedSeats) {
                        int seatIndex = allSeats.indexOf(seat); // Find index in the main list
                        if (seatIndex == -1) {
                            // Should not happen if logic is correct
                            throw new SQLException("Seat not found in showtime layout");
                        }

                        pstmt.setInt(1, bookingId);
                        pstmt.setInt(2, seatIndex);
                        pstmt.setString(3, seat.getSeatType().toString());
                        pstmt.setDouble(4, seat.getPrice());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks if a user exists by NIC, returns their ID, or creates a new user and
     * returns the new ID.
     */
    private int getOrCreateUser(User user, Connection conn) throws SQLException {
        // Check if user exists by NIC
        String selectUser = "SELECT user_id FROM users WHERE nic = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectUser)) {
            pstmt.setString(1, user.getNic());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }

        // Create new user
        String insertUser = "INSERT INTO users (nic, name, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getNic());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create user.");
    }
}
