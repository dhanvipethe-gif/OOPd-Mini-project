package com.restaurant.users;

public class VIPCustomer extends User {
    public VIPCustomer(String name) { super(name); }

    @Override
    public double getDiscount(double total) {
        return total * 0.20; // 20% discount
    }
}
