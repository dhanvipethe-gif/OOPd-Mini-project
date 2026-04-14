package com.restaurant.services;

public class TransactionService {

    //Dine-in
    public double calcTotal(double price) {
        return price; 
    }

    //Delivery (adds fee)
    public double calcTotal(double price, double deliveryFee) {
        return price + deliveryFee;
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
