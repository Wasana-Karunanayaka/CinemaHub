CREATE DATABASE IF NOT EXISTS cinemahub;
USE cinemahub;

-- 1. Movies Table
CREATE TABLE IF NOT EXISTS movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    language VARCHAR(50) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    format VARCHAR(20) NOT NULL, -- e.g., '2D', '3D'
    imdb_rating DECIMAL(3, 1),
    release_date VARCHAR(20), -- Keeping as string to match Java 'String releaseDate'
    duration INT NOT NULL -- in minutes
);

-- 2. ShowTimes Table
CREATE TABLE IF NOT EXISTS showtimes (
    showtime_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    day VARCHAR(20) NOT NULL, -- e.g., 'Mon', 'Tue'
    show_time VARCHAR(20) NOT NULL, -- e.g., '16:00'
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE
);

-- 3. Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    nic VARCHAR(50) NOT NULL UNIQUE, -- National ID as unique identifier
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- 4. Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    showtime_id INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id)
);

-- 5. Booking Seats Table
-- Maps specific seats to a booking. 
-- Since the application uses a fixed list of 100 seats per showtime (Indices 0-99), 
-- we store the 'seat_index' to reconstruct which seats are taken.
CREATE TABLE IF NOT EXISTS booking_seats (
    booking_seat_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    seat_index INT NOT NULL, -- 0 to 99
    seat_type VARCHAR(20) NOT NULL, -- 'STANDARD', 'PREMIUM', 'VIP'
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE,
    UNIQUE KEY unique_seat_per_show (booking_id, seat_index) 
);
