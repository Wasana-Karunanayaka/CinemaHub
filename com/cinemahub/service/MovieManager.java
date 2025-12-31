package com.cinemahub.service;

import com.cinemahub.dao.MovieDAO;
import com.cinemahub.dao.MovieDAOImpl;
import com.cinemahub.model.Movie;
import com.cinemahub.model.ShowTime;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class that handles storing, loading, searching, and saving movie
 * data.
 * 
 * @author Wasana Karunanayaka
 */
public class MovieManager {
    private static List<Movie> movies = new ArrayList<>(); // List to store movies
    private static MovieManager movieManager; // Singleton instance
    private final MovieDAO movieDAO = new MovieDAOImpl(); // DAO instance

    // private constructor
    private MovieManager() {
        refreshMovies(); // Load movies from DB
    }

    // Get the Singleton instance
    public static synchronized MovieManager getMovieManager() {
        if (movieManager == null)
            movieManager = new MovieManager();
        return movieManager;
    }

    /**
     * Displays all movies to the console.
     */
    public void displayMovies() {
        // refreshMovies(); // Optional: Fetch latest from DB to ensure up-to-date view
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        for (Movie movie : movies) {
            System.out.println(movie.getMovieDetails());
        }
    }

    /**
     * Search for a movie by title and return the movie object if found.
     * 
     * @param title The title to search for.
     * @return The Movie object if found, otherwise null.
     */
    public Movie searchMovie(String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Display timetable for all movies.
     */
    public void displayTimeTable() {
        if (movies.isEmpty()) {
            System.out.println("No movies available for timetable display.");
            return;
        }

        for (Movie movie : movies) {
            System.out.println("Movie: " + movie.getTitle());
            for (ShowTime showTime : movie.getShowTimes()) {
                System.out.println(" - " + showTime.getShowTimeInfo());
            }
        }
    }

    /**
     * Adds a new movie to the local list and saves it to the database.
     * 
     * @param movie The movie to add.
     */
    public void addMovie(Movie movie) {
        movies.add(movie);
        movieDAO.saveMovie(movie);
        System.out.println("Movie saved to database.");
    }

    /**
     * Removes a movie from the local list and deletes it from the database.
     * 
     * @param movie The movie to remove.
     */
    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movieDAO.deleteMovie(movie);
        System.out.println("Movie deleted from database.");
    }

    /**
     * Adds a showtime to a movie and persists it to the database.
     * 
     * @param showTime The showtime to add.
     * @param movie    The movie to which the showtime belongs.
     */
    public void addShowTime(ShowTime showTime, Movie movie) {
        movieDAO.addShowTime(showTime, movie.getId());
    }

    /**
     * Refresh local cache from DB.
     * Fetches all movies from the database including their showtimes.
     */
    public void refreshMovies() {
        movies = movieDAO.getAllMovies();
    }

    // Getter
    protected List<Movie> getMovies() {
        return movies;
    }
}
