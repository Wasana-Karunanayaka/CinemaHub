package com.cinemahub.model;

/**
 * Represents a user with details like name, NIC, and email for booking
 * purposes.
 * 
 * @author Wasana Karunanayaka
 */
public class User {
    private String name; // User's full name
    private String nic; // National ID
    private String email; // Email address

    // Constructor
    public User(String name, String nic, String email) {
        this.name = name;
        this.nic = nic;
        this.email = email;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getNic() {
        return nic;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
