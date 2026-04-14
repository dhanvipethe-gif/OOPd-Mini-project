package com.restaurant.users;

public class Employee extends User {
    public Employee(String name) { super(name); }

    @Override
    public double getDiscount(double total) {
        return total * 0.50; // 50% discount for staff
    }
}
