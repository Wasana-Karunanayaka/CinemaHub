package com.cinemahub.dao;

import com.cinemahub.model.Booking;

/**
 * Interface definition for Booking Data Access Object.
 * 
 * @author Wasana Karunanayaka
 */
public interface BookingDAO {
    void saveBooking(Booking booking);
}
