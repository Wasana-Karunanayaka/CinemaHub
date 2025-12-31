package com.cinemahub.ui;

import com.cinemahub.model.Movie;
import com.cinemahub.model.ShowTime;
import com.cinemahub.service.MovieManager;

import java.util.Scanner;

/**
 * Handles admin functionalities like adding/removing movies and managing
 * showtimes.
 * 
 * @author Wasana Karunanayaka
 */
public class Admin {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";
    private final MovieManager movieManager = MovieManager.getMovieManager();
    private final Scanner scanner;

    // Constructor to initialize Scanner
    public Admin(Scanner scanner) {
        this.scanner = scanner;
    }

    // Validates admin credentials
    public static boolean validateCredentials(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    // Admin menu options
    public void adminMenu() {
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add Movie");
            System.out.println("2. Remove Movie");
            System.out.println("3. Add Showtime");
            System.out.println("4. Logout");
            System.out.print("Select an option: ");

            int choice = CinemaHub.getValidatedChoice(scanner);

            switch (choice) {
                case 1 -> addMovie();
                case 2 -> removeMovie();
                case 3 -> addShowTime();
                case 4 -> adminRunning = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Adds a new movie
    private void addMovie() {
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();
        System.out.print("Enter movie language: ");
        String language = scanner.nextLine();
        System.out.print("Enter movie genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter movie format (2D/3D): ");
        String format = scanner.nextLine();
        System.out.print("Enter IMDB rating: ");
        double rating = validateDoubleInput("Enter a valid IMDB rating (0-10): ");
        System.out.print("Enter release date (YYYY-MM-DD): ");
        String releaseDate = scanner.nextLine();
        System.out.print("Enter duration (in minutes): ");
        int duration = CinemaHub.getValidatedChoice(scanner);

        // Create and add the movie
        Movie movie = new Movie(title, language, genre, format, rating, releaseDate, duration);
        // Correctly delegate to manager which handles DAO
        movieManager.addMovie(movie);
        System.out.println("Movie added: " + movie.getTitle());
    }

    // Removes a movie
    private void removeMovie() {
        System.out.print("Enter the title of the movie to remove: ");
        String title = scanner.nextLine();

        Movie movie = movieManager.searchMovie(title);
        if (movie != null) {
            movieManager.removeMovie(movie);
            System.out.println("Movie removed: " + title);
        } else {
            System.out.println("Movie not found.");
        }
    }

    // Adds a showtime to a movie
    private void addShowTime() {
        System.out.print("Enter the title of the movie to update: ");
        String title = scanner.nextLine();

        Movie movie = movieManager.searchMovie(title);
        if (movie != null) {
            System.out.print("Enter showtime day: ");
            String day = scanner.nextLine();
            System.out.print("Enter showtime time: ");
            String time = scanner.nextLine();

            ShowTime newShowTime = new ShowTime(day, time);
            movie.getShowTimes().add(newShowTime);
            movieManager.addShowTime(newShowTime, movie);
            System.out.println("Showtime added for movie: " + movie.getTitle());
            System.out.println("Showtime successfully persisted to Database.");
        } else {
            System.out.println("Movie not found.");
        }
    }

    // Validates and returns a double input
    private double validateDoubleInput(String errorMessage) {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (Exception e) {
                System.out.print(errorMessage);
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
