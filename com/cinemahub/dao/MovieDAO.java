package com.cinemahub.dao;

import com.cinemahub.model.Movie;
import com.cinemahub.model.ShowTime;
import java.util.List;

/**
 * Interface definition for Movie Data Access Object.
 * 
 * @author Wasana Karunanayaka
 */
public interface MovieDAO {
    List<Movie> getAllMovies();

    void saveMovie(Movie movie);

    void deleteMovie(Movie movie);

    void addShowTime(ShowTime showTime, int movieId);
}
