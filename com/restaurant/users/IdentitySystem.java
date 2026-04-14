package com.restaurant.users;

public class IdentitySystem {
    public static void main(String[] args) {
        AuthService auth = new AuthService(); // We will create this next
        User user1 = auth.login("Bob");
        
        if (user1 != null) {
            System.out.println("User: " + user1.getUsername());
            System.out.println("Discount: " + user1.getDiscount(100));
        }
    }
}
