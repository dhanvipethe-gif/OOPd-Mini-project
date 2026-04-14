package com.restaurant.services;
import com.restaurant.users.User;

public class TransactionService {

    //Dine-in
    public double calcTotal(User user, double price) {
        double discount = user.getDiscount(price); // Calling Member 1's method
        double finalPrice = price - discount;
        System.out.println("Applying discount for " + user.getUsername() + ": -$" + discount);
        return finalPrice;
    }

    //delivery
    public double calcTotal(User user, double price, double deliveryFee) {
        double priceAfterDiscount = calcTotal(user, price); 
        return priceAfterDiscount + deliveryFee;
    }

    //Payment method with Exception Handling
    public void processPayment(double amount, double balance) throws InsufficientFundsException {
        try {
            System.out.println("Processing payment of: " + amount);
            if (amount > balance) {
                throw new InsufficientFundsException("Error: Balance is too low!");
            }
            System.out.println("Payment successful! Remaining: " + (balance - amount));
        } catch (InsufficientFundsException e) {
            System.out.println("Caught in Service: " + e.getMessage());
            throw e; // Rethrowing to let the UI know
        } finally {
            System.out.println("Transaction log updated."); // Always executes
        }
    }
}
