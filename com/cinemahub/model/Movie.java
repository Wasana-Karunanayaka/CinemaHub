package com.cinemahub.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a movie with attributes like title, genre, language, and
 * associated showtimes.
 * 
 * @author Wasana Karunanayaka
 */
public class Movie {
    private int id; // Database ID
    private String title;
    private String language;
    private String genre;
    private String format;
    private double imdbRating;
    private String releaseDate;
    private int duration;
    private List<ShowTime> showTimes;

    // Constructor
    public Movie(String title, String language, String genre, String format, double imdbRating,
            String releaseDate, int duration) {
        this.title = title;
        this.language = language;
        this.genre = genre;
        this.format = format;
        this.imdbRating = imdbRating;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.showTimes = new ArrayList<>();
    }

    // Method to return movie details
    public String getMovieDetails() {
        return "\nTitle: " + title +
                "\nLanguage: " + language +
                "\nGenre: " + genre +
                "\nFormat: " + format +
                "\nIMDb Rating: " + imdbRating +
                "\nRelease Date: " + releaseDate +
                "\nDuration: " + duration + " minutes";
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public String getGenre() {
        return genre;
    }

    public String getFormat() {
        return format;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public List<ShowTime> getShowTimes() {
        return showTimes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
