package com.restaurant.users;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users = new ArrayList<>();

    public AuthService() {
        // Pre-fill with some test users
        users.add(new VIPCustomer("Bob"));
        users.add(new Employee("Charlie"));
    }

    public User login(String name) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(name)) return u;
        }
        return null; 
    }
}
