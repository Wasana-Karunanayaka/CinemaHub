package com.cinemahub.ui;

import java.util.Scanner;

/**
 * The main class, acting as the entry point for the program, managing user and
 * admin navigation.
 * 
 * @author Wasana Karunanayaka
 */
public class CinemaHub {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) { // Use try-with-resources to ensure scanner closes
                                                         // automatically
            System.out.println("****** Welcome to CinemaHub! ******");
            boolean running = true;

            while (running) {
                // Display main menu
                System.out.println("\nLogin as:");
                System.out.println("1. Admin");
                System.out.println("2. User");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                int choice = getValidatedChoice(scanner); // Input validation for menu choice

                switch (choice) {
                    case 1 -> adminLogin(scanner);
                    case 2 -> userLogin(scanner);
                    case 3 -> {
                        running = false;
                        System.out.println("Thank you for using CinemaHub!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    // Handles Admin login
    private static void adminLogin(Scanner scanner) {
        System.out.print("\nEnter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin password: ");
        String password = scanner.nextLine();

        if (Admin.validateCredentials(username, password)) {
            Admin admin = new Admin(scanner); // Pass scanner to Admin class
            admin.adminMenu();
        } else {
            System.out.println("Invalid Admin credentials.");
        }
    }

    // Handles User login
    private static void userLogin(Scanner scanner) {
        UserInteraction userInteraction = new UserInteraction(scanner);
        userInteraction.userMenu();
    }

    // Input validation to ensure a valid integer is entered
    public static int getValidatedChoice(Scanner scanner) {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return choice;
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a valid number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
