package com.cinemahub.ui;

import com.cinemahub.model.Movie;
import com.cinemahub.model.Seat;
import com.cinemahub.model.ShowTime;
import com.cinemahub.model.User;
import com.cinemahub.service.BookingManager;
import com.cinemahub.service.MovieManager;

import java.util.List;
import java.util.Scanner;

/**
 * Manages user operations such as viewing movies, searching, and booking
 * tickets.
 * 
 * @author Wasana Karunanayaka
 */
public class UserInteraction {
    private final Scanner scanner;

    // Constructor to initialize Scanner
    public UserInteraction(Scanner scanner) {
        this.scanner = scanner;
    }

    // User menu options
    public void userMenu() {
        boolean userRunning = true;

        while (userRunning) {
            System.out.println("\n****** Welcome to CinemaHub QuickTickets ******");
            System.out.println("1. View Movies");
            System.out.println("2. Search Movies");
            System.out.println("3. View Timetable");
            System.out.println("4. Book Tickets");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            int choice = CinemaHub.getValidatedChoice(scanner);

            switch (choice) {
                case 1 -> MovieManager.getMovieManager().displayMovies();
                case 2 -> searchAndDisplayMovies();
                case 3 -> MovieManager.getMovieManager().displayTimeTable();
                case 4 -> getBookingDetails();
                case 5 -> {
                    userRunning = false;
                    System.out.println("Logging out. Thank you for using CinemaHub!");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Get booking details from the user
    private void getBookingDetails() {
        BookingManager bookingManager = new BookingManager();

        // Collect user details
        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter your NIC: ");
        String userNic = scanner.nextLine();
        System.out.print("Enter your email: ");
        String userEmail = scanner.nextLine();
        User user = new User(userName, userNic, userEmail);

        // Display available movies
        System.out.println("\n*** Available Movies ***");
        MovieManager.getMovieManager().displayMovies();
        System.out.print("\nEnter movie title to book: ");
        String title = scanner.nextLine();

        // Find the movie
        Movie selectedMovie = MovieManager.getMovieManager().searchMovie(title);
        if (selectedMovie == null) {
            System.out.println("No movies found with title: " + title);
            return;
        }

        // Show available showtimes
        System.out.println("Available showtimes for " + title + ":");
        List<ShowTime> showTimes = selectedMovie.getShowTimes();
        for (int i = 0; i < showTimes.size(); i++) {
            System.out.println((i + 1) + ". " + showTimes.get(i).getShowTimeInfo());
        }

        // Input validation for showtime
        int showtimeChoice = -1;
        while (showtimeChoice < 1 || showtimeChoice > showTimes.size()) {
            System.out.print("Select a showtime (e.g., 1, 2): ");
            showtimeChoice = CinemaHub.getValidatedChoice(scanner);
            if (showtimeChoice < 1 || showtimeChoice > showTimes.size()) {
                System.out.println("Invalid choice. Please select a valid showtime.");
            }
        }

        ShowTime selectedShowTime = showTimes.get(showtimeChoice - 1);

        // Display available seats for each seat type
        System.out.println("\nRemaining seats for each seat type:");
        for (Seat.SeatType seatType : Seat.SeatType.values()) {
            int availableSeats = countAvailableSeats(selectedShowTime, seatType);
            System.out.println(seatType + ": " + availableSeats + " remaining");
        }

        // Seat selection with input validation
        String seatType = selectSeatType();

        System.out.print("Enter the number of seats: ");
        int seatCount = CinemaHub.getValidatedChoice(scanner);

        // Process the booking
        bookingManager.processBooking(selectedMovie, selectedShowTime, user, seatType, seatCount);
    }

    // Search for a movie by title and display details
    private void searchAndDisplayMovies() {
        System.out.print("Enter movie title to search: ");
        String title = scanner.nextLine();

        Movie movie = MovieManager.getMovieManager().searchMovie(title);
        if (movie != null) {
            System.out.println(movie.getMovieDetails());
            System.out.println("Show Times:");
            for (ShowTime showTime : movie.getShowTimes()) {
                System.out.println(" - " + showTime.getShowTimeInfo());
            }
        } else {
            System.out.println("No movies found with the title: " + title);
        }
    }

    // Seat selection with input validation
    private String selectSeatType() {
        int seatChoice = -1;
        String seatType = "";

        while (seatChoice < 1 || seatChoice > 3) {
            System.out.println("\nSelect seat type:");
            System.out.println("1. STANDARD (Rs.500)");
            System.out.println("2. PREMIUM (Rs.750)");
            System.out.println("3. VIP (Rs.1000)");
            System.out.print("Enter your choice (1, 2, 3): ");
            seatChoice = CinemaHub.getValidatedChoice(scanner);

            switch (seatChoice) {
                case 1 -> seatType = "STANDARD";
                case 2 -> seatType = "PREMIUM";
                case 3 -> seatType = "VIP";
                default -> System.out.println("Invalid seat type selected. Please try again.");
            }
        }

        return seatType;
    }

    // Count available seats for a specific seat type and showtime
    private int countAvailableSeats(ShowTime showTime, Seat.SeatType seatType) {
        int availableSeats = 0;
        for (Seat seat : showTime.getSeats()) {
            if (seat.getSeatType().toString() == seatType.toString() && seat.isAvailable()) {
                availableSeats++;
            }
        }
        return availableSeats;
    }
}
