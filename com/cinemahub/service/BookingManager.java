package com.cinemahub.service;

import com.cinemahub.dao.BookingDAO;
import com.cinemahub.dao.BookingDAOImpl;
import com.cinemahub.model.Booking;
import com.cinemahub.model.Movie;
import com.cinemahub.model.Seat;
import com.cinemahub.model.ShowTime;
import com.cinemahub.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages booking operations, including seat selection, availability checks,
 * and finalizing bookings.
 * 
 * @author Wasana Karunanayaka
 */
public class BookingManager {
    private final List<Booking> bookings = new ArrayList<>();
    private final BookingDAO bookingDAO = new BookingDAOImpl();

    /**
     * Handles the complete booking process: checks availability, reserves seats,
     * calculates price, creates the booking object, and saves it.
     * 
     * @param movie     The movie to book.
     * @param showTime  The selected showtime.
     * @param user      The user making the booking.
     * @param seatType  The desired type of seat (e.g., STANDARD).
     * @param seatCount The number of seats to book.
     */
    public void processBooking(Movie movie, ShowTime showTime, User user, String seatType, int seatCount) {
        // Check seat availability
        if (!checkSeatAvailability(showTime, seatCount, seatType)) {
            System.out.println("Not enough seats available.");
            return;
        }

        // Reserve seats
        List<Seat> selectedSeats = new ArrayList<>();
        for (Seat seat : showTime.getSeats()) {
            if (seat.getSeatType().toString().equalsIgnoreCase(seatType) && seat.isAvailable()) {
                seat.reserve();
                selectedSeats.add(seat);
                if (selectedSeats.size() == seatCount)
                    break;
            }
        }

        // Confirm reservation
        System.out.println("Seats reserved successfully.");

        // Calculate total price
        double totalPrice = calculateTotalPrice(selectedSeats);

        // Create a booking
        Booking booking = new Booking(user, movie, showTime, selectedSeats, totalPrice);

        // Save and display booking
        confirmBooking(booking);
        System.out.println("\nBooking successful! Here are your details:\n" + booking.generateBookingSummary());
    }

    /**
     * Check seat availability for a specific showtime.
     * 
     * @param showTime  The showtime to check.
     * @param seatCount Number of seats required.
     * @param seatType  The type of seat.
     * @return True if enough seats are available, false otherwise.
     */
    public boolean checkSeatAvailability(ShowTime showTime, int seatCount, String seatType) {
        int availableSeats = 0;
        for (Seat seat : showTime.getSeats()) {
            if (seat.getSeatType().toString().equalsIgnoreCase(seatType) && seat.isAvailable()) {
                availableSeats++;
            }
        }
        return availableSeats >= seatCount;
    }

    /**
     * Calculate the total price based on selected seats.
     * 
     * @param selectedSeats List of selected seats.
     * @return Total price of the seats.
     */
    public double calculateTotalPrice(List<Seat> selectedSeats) {
        double total = 0;
        for (Seat seat : selectedSeats) {
            total += seat.getPrice();
        }
        return total;
    }

    /**
     * Confirm and save the booking using the DAO.
     * 
     * @param booking The booking to save.
     */
    public void confirmBooking(Booking booking) {
        bookings.add(booking);
        // Use DAO to save to database
        bookingDAO.saveBooking(booking);
    }
}
