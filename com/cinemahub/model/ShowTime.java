package com.cinemahub.model;

import java.util.List;

/**
 * Represents a specific showtime for a movie, including day, time, and seat
 * availability.
 * 
 * @author Wasana Karunanayaka
 */
public class ShowTime {
    // Attributes
    private int id; // Database ID
    private String day;
    private String time;
    private List<Seat> seats; // List of seats for this showtime

    // Constructor
    public ShowTime(String day, String time) {
        this.day = day;
        this.time = time;
        this.seats = Seat.createDefaultLayout(); // Initialize with a fresh seat layout
    }

    // Method to print showTimes
    public String getShowTimeInfo() {
        return "Day: " + day + ", Time: " + time;
    }

    // Reset seat availability for new showtimes
    public void resetSeatAvailability() {
        for (Seat seat : seats) {
            seat.setAvailable(true);
        }
    }

    // Getters
    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
