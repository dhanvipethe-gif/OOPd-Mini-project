package com.restaurant.users;

public abstract class User {
    private String username;
    public User(String username) { this.username = username; }
    public String getUsername() { return username; }
    public abstract double calculateDiscount(double totalAmount);
}
