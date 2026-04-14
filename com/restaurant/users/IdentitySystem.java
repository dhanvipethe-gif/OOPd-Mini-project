package com.restaurant.users;

public class IdentitySystem {
    public static void main(String[] args) {
        User user = new VIPCustomer("Bob");
        System.out.println("User: " + user.getUsername());
        System.out.println("Discount: " + user.calculateDiscount(100));
    }
}
