package com.cinemahub.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines seat properties like type (STANDARD, PREMIUM, VIP), availability, and
 * price.
 * 
 * @author Wasana Karunanayaka
 */
public class Seat {
    public enum SeatType {
        STANDARD, PREMIUM, VIP
    } // Seat types

    private SeatType seatType;
    private boolean isAvailable;
    private double price;

    // Constructor
    public Seat(SeatType seatType, double price) {
        this.seatType = seatType;
        this.price = price;
        this.isAvailable = true; // Default: seat is available
    }

    // Factory method to generate a fresh layout of new Seat objects
    public static List<Seat> createDefaultLayout() {
        List<Seat> layout = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            layout.add(new Seat(SeatType.STANDARD, 500.0));
        for (int i = 0; i < 30; i++)
            layout.add(new Seat(SeatType.PREMIUM, 750.0));
        for (int i = 0; i < 20; i++)
            layout.add(new Seat(SeatType.VIP, 1000.0));
        return layout;
    }

    // Reserve a seat
    public void reserve() {
        if (isAvailable) {
            isAvailable = false;
        }
    }

    // Getters
    public double getPrice() {
        return price;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Set availability
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
