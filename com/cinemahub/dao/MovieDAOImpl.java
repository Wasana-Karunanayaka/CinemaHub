package com.cinemahub.dao;

import com.cinemahub.model.Movie;
import com.cinemahub.model.Seat;
import com.cinemahub.model.ShowTime;
import com.cinemahub.util.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of MovieDAO using JDBC.
 * 
 * @author Wasana Karunanayaka
 */
public class MovieDAOImpl implements MovieDAO {

    /**
     * Retrieves all movies, their showtimes, and seat availability from the
     * database.
     */
    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String movieQuery = "SELECT * FROM movies";

        try (Connection conn = DatabaseHelper.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(movieQuery)) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getString("title"),
                        rs.getString("language"),
                        rs.getString("genre"),
                        rs.getString("format"),
                        rs.getDouble("imdb_rating"),
                        rs.getString("release_date"),
                        rs.getInt("duration"));
                movie.setId(rs.getInt("movie_id"));

                // Load showtimes for this movie
                loadShowTimes(movie, conn);

                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Loads showtimes for a specific movie and marks booked seats.
     */
    private void loadShowTimes(Movie movie, Connection conn) throws SQLException {
        String showTimeQuery = "SELECT * FROM showtimes WHERE movie_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(showTimeQuery)) {
            pstmt.setInt(1, movie.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ShowTime showTime = new ShowTime(
                            rs.getString("day"),
                            rs.getString("show_time"));
                    showTime.setId(rs.getInt("showtime_id"));

                    // Mark booked seats as unavailable
                    markBookedSeats(showTime, conn);

                    movie.getShowTimes().add(showTime);
                }
            }
        }
    }

    /**
     * Identifies which seats are booked for a showtime and marks them as
     * unavailable.
     */
    private void markBookedSeats(ShowTime showTime, Connection conn) throws SQLException {
        // Query to get booked seat indices for this showtime
        String seatQuery = "SELECT bs.seat_index FROM booking_seats bs " +
                "JOIN bookings b ON bs.booking_id = b.booking_id " +
                "WHERE b.showtime_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(seatQuery)) {
            pstmt.setInt(1, showTime.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Seat> seats = showTime.getSeats();
                while (rs.next()) {
                    int seatIndex = rs.getInt("seat_index");
                    if (seatIndex >= 0 && seatIndex < seats.size()) {
                        seats.get(seatIndex).setAvailable(false);
                    }
                }
            }
        }
    }

    /**
     * Saves a new movie and its showtimes to the database transactionally.
     */
    @Override
    public void saveMovie(Movie movie) {
        String insertMovie = "INSERT INTO movies (title, language, genre, format, imdb_rating, release_date, duration) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertShowTime = "INSERT INTO showtimes (movie_id, day, show_time) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert Movie
            int movieId = -1;
            try (PreparedStatement movieStmt = conn.prepareStatement(insertMovie, Statement.RETURN_GENERATED_KEYS)) {
                movieStmt.setString(1, movie.getTitle());
                movieStmt.setString(2, movie.getLanguage());
                movieStmt.setString(3, movie.getGenre());
                movieStmt.setString(4, movie.getFormat());
                movieStmt.setDouble(5, movie.getImdbRating());
                movieStmt.setString(6, movie.getReleaseDate());
                movieStmt.setInt(7, movie.getDuration());
                movieStmt.executeUpdate();

                try (ResultSet generatedKeys = movieStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        movieId = generatedKeys.getInt(1);
                        movie.setId(movieId);
                    }
                }
            }

            // Insert ShowTimes
            if (movieId != -1) {
                try (PreparedStatement showTimeStmt = conn.prepareStatement(insertShowTime,
                        Statement.RETURN_GENERATED_KEYS)) {
                    for (ShowTime st : movie.getShowTimes()) {
                        showTimeStmt.setInt(1, movieId);
                        showTimeStmt.setString(2, st.getDay());
                        showTimeStmt.setString(3, st.getTime());
                        showTimeStmt.executeUpdate();

                        try (ResultSet stKeys = showTimeStmt.getGeneratedKeys()) {
                            if (stKeys.next()) {
                                st.setId(stKeys.getInt(1));
                            }
                        }
                    }
                }
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteMovie(Movie movie) {
        String deleteQuery = "DELETE FROM movies WHERE movie_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, movie.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addShowTime(ShowTime showTime, int movieId) {
        String insertQuery = "INSERT INTO showtimes (movie_id, day, show_time) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, movieId);
            pstmt.setString(2, showTime.getDay());
            pstmt.setString(3, showTime.getTime());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    showTime.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
