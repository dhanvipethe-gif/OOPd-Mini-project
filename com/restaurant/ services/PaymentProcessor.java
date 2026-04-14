package com.restaurant.services;

public class PaymentProcessor implements IPayable {
    private String status = "Pending";

    @Override
    public void run() {
        // This is where you would normally put the thread logic
    }

    @Override
    public void processPayment(double amount, double balance) throws PaymentException {
        System.out.println("[PaymentProcessor] Attempting to charge: ₹" + amount);
        
        if (balance < amount) {
            status = "Failed";
            throw new PaymentException("Transaction Declined: Insufficient balance.");
        }

        status = "Success";
        System.out.println("[PaymentProcessor] Payment Successful! Remaining: ₹" + (balance - amount));
    }

    @Override
    public String getPaymentStatus() {
        return status;
    }
}
