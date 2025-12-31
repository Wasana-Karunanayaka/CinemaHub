package com.cinemahub.model;

import java.util.List;

/**
 * Manages booking details, including user, movie, showtime, seats, and total
 * cost.
 * 
 * @author Wasana Karunanayaka
 */
public class Booking {
    private User user; // User details
    private Movie movie; // Movie details
    private ShowTime showTime; // Showtime details
    private List<Seat> seats; // Booked seats
    private double totalPrice; // Total price of the booking

    // Constructor
    public Booking(User user, Movie movie, ShowTime showTime, List<Seat> seats, double totalPrice) {
        this.user = user;
        this.movie = movie;
        this.showTime = showTime;
        this.seats = seats;
        this.totalPrice = totalPrice;
    }

    // Generating Booking Summary
    public String generateBookingSummary() {
        String summary = "*** Booking Summary ***\n";
        summary += "User: " + user.getName() + "\n" +
                "NIC: " + user.getNic() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Movie: " + movie.getTitle() + "\n" +
                "Showtime: " + showTime.getDay() + " at " + showTime.getTime() + "\n" +
                "Seat Type: " + seats.get(0).getSeatType() + "\n" +
                "No of Seats: " + seats.size() + "\n" +
                "Total Price: Rs." + totalPrice;
        return summary;
    }

    // Getters for DAO
    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public ShowTime getShowTime() {
        return showTime;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Save the booking details to a file - DEPRECATED/REMOVED
    // proper persistence is handled by BookingDAO
    public void saveBooking() {
        // No-op or removed. Logic moved to BookingDAO.
    }
}
