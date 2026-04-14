package com.restaurant.services;

//Custom Exception Hierarchy
public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends PaymentException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
