package com.restaurant.services;

import com.restaurant.users.User;

public class TransactionService {
    private PaymentProcessor processor = new PaymentProcessor();

    // Overload 1: Dine-in (Simple)
    public void processOrder(User user, double amount) throws PaymentException {
        // Calls Member 1's getDiscount method
        double finalAmount = user.getDiscount(amount);
        
        System.out.println("--- Processing Dine-in Order for " + user.getUsername() + " ---");
        // We assume a sample balance for this demo
        processor.processPayment(finalAmount, 1000.0); 
    }

    // Overload 2: Delivery (Includes an extra parameter for address)
    public void processOrder(User user, double amount, String address) throws PaymentException {
        double deliveryFee = 40.0;
        double finalAmount = user.getDiscount(amount) + deliveryFee;
        
        System.out.println("--- Processing Delivery Order for " + user.getUsername() + " ---");
        System.out.println("Address: " + address);
        processor.processPayment(finalAmount, 1000.0);
    }
}
