# CinemaHub

CinemaHub is a console-based Movie Ticket Booking System built using Java and MySQL. It allows administrators to manage movies and showtimes, and enables users to browse movies, check availability, and book tickets seamlessly.

## 🚀 Features

### 👤 User Features
- **View Movies:** Browse a list of all available movies with details like rating, genre, and duration.
- **Search:** Find specific movies by title.
- **View Timetable:** Check showtimes for all movies.
- **Book Tickets:** Select a movie, showtime, and seat category (Standard, Premium, VIP) to book tickets.
- **Dynamic Pricing:** Ticket prices vary based on the selected seat category.

### 🛡️ Admin Features
- **Secure Login:** Protected administrative access.
- **Add Movies:** Add new movie details to the system.
- **Remove Movies:** Delete old or outdated movies.
- **Manage Showtimes:** Add new showtimes for existing movies.

## 🛠️ Technology Stack
- **Language:** Java (JDK 21+)
- **Database:** MySQL
- **Architecture:** DAO (Data Access Object) Pattern with Layered Architecture (Model-View-Service).
- **Driver:** MySQL Connector/J

## 📂 Project Structure
The project is organized into modular packages for better maintainability:

- `com.cinemahub.model` - POJO classes representing entities (Movie, User, Booking, etc.).
- `com.cinemahub.dao` - Data Access Objects handling direct database operations.
- `com.cinemahub.service` - Business logic and managers (`MovieManager`, `BookingManager`).
- `com.cinemahub.ui` - Console-based user interface and entry point (`CinemaHub`).
- `com.cinemahub.util` - Utility classes (`DatabaseHelper`).

## ⚙️ Prerequisites
Before running the project, ensure you have:
1.  **Java Development Kit (JDK):** Version 21 or higher.
2.  **MySQL Server:** Installed and running.
3.  **MySQL JDBC Driver:** `mysql-connector-j-9.5.0.jar` (or compatible version).

## 📥 Setup & Installation

1.  **Clone/Download the Project:**
    Ensure all files are in the `CinemaHub` directory.

2.  **Database Configuration:**
    - Open your MySQL client (Workbench or CLI).
    - Run the provided `schema.sql` script to create the database and tables.
    - Update `com/cinemahub/util/DatabaseHelper.java` with your MySQL credentials:
      ```java
      private static final String USER = "your_username";
      private static final String PASSWORD = "your_password";
      ```

3.  **JDBC Driver:**
    - Ensure the `mysql-connector-j-9.5.0.jar` is available.
    - If needed, update the path in `run_project.ps1`.

## ▶️ How to Run
We have provided a PowerShell script to automate compilation and execution.

### Using PowerShell (Recommended)
Open a terminal in the project root and run:
```powershell
.\run_project.ps1
```

### Manual Compilation & Execution
If you prefer to run it manually:
1.  **Compile:**
    ```bash
    javac -d out -cp ".;path/to/mysql-connector.jar" com/cinemahub/*/*.java
    ```
2.  **Run:**
    ```bash
    java -cp "out;path/to/mysql-connector.jar" com.cinemahub.ui.CinemaHub
    ```

## 👨‍💻 Author
**Wasana Karunanayaka**

---
*Note: This project is intended for educational purposes.*
