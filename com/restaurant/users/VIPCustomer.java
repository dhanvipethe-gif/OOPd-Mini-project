package com.restaurant.users;

public class VIPCustomer extends User {
    public VIPCustomer(String name) { super(name); }
    @Override
    public double calculateDiscount(double total) { return total * 0.20; }
}
