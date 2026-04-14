package com.restaurant.users;

public abstract class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    // Renamed to match the team agreement
    public abstract double getDiscount(double totalAmount);
}
