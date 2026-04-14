package com.restaurant.services;

/**
 * IPayable Interface
 * Defines the contract for payment processing.
 */
public interface IPayable {
    // The core method to handle money
    void processPayment(double amount, double balance) throws PaymentException;
    
    // To check if the payment went through
    String getPaymentStatus();
}
