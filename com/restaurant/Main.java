package com.restaurant;

// ── Member 1's package ────────────────────────────────────────────────────────
import com.restaurant.users.*;

// ── Member 2's package (YOUR work) ───────────────────────────────────────────
import com.restaurant.menu.*;

// ── Member 3's package ────────────────────────────────────────────────────────
import com.restaurant.services.*;

import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  CAFE COMPANION — Integration Entry Point                        ║
 * ║  Wires together all three members' packages.                     ║
 * ║                                                                  ║
 * ║  Flow: Login → Browse Menu → Place Order → Reserve Table → Pay  ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("╔═════════════════════════════════════╗");
        System.out.println("║   Welcome to Cafe Companion ☕       ║");
        System.out.println("╚═════════════════════════════════════╝\n");

        // ────────────────────────────────────────────────────────────────────
        // STEP 1 — MEMBER 1: Login / Identity
        // AuthService is pre-filled with "Bob" (VIPCustomer) and "Charlie" (Employee)
        // ────────────────────────────────────────────────────────────────────
        System.out.println("══ STEP 1: Authentication (Member 1) ══");
        AuthService auth = new AuthService();

        User bob     = auth.login("Bob");      // VIPCustomer → 20% discount
        User charlie = auth.login("Charlie");  // Employee    → 50% discount
        User unknown = auth.login("Alice");    // Not in system → null

        printUser(bob);
        printUser(charlie);
        if (unknown == null) {
            System.out.println("Login failed for: Alice (not registered)\n");
        }

        // ────────────────────────────────────────────────────────────────────
        // STEP 2 — MEMBER 2 (YOUR ROLE): Load Menu with Generics
        // ────────────────────────────────────────────────────────────────────
        System.out.println("══ STEP 2: Menu Loading (Member 2 — Generics) ══");
        MenuManager menuManager = new MenuManager();
        menuManager.printAllMenus();

        // Get today's flat item list for OrderProcessor
        List<MenuItem> dailyMenu = menuManager.getDailyMenu();
        System.out.println("\nTotal items available today: " + dailyMenu.size());

        // ────────────────────────────────────────────────────────────────────
        // STEP 3 — MEMBER 3: Multi-threaded Order Processing
        // OrderProcessor.startOrderingSystem() uses Member 2's List<MenuItem>
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\n══ STEP 3: Order Processing Threads (Member 3) ══");
        OrderProcessor.startOrderingSystem(dailyMenu);

        // ────────────────────────────────────────────────────────────────────
        // STEP 4 — MEMBER 3: Reservation System (synchronized threads)
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\n══ STEP 4: Table Reservations (Member 3 — Threads) ══");

        Thread res1 = new Thread(new ReservationSystem("Bob",     2));
        Thread res2 = new Thread(new ReservationSystem("Charlie", 2)); // same table → conflict!
        Thread res3 = new Thread(new ReservationSystem("Alice",   5));

        res1.start();
        res2.start();
        res3.start();

        // Wait for reservation threads to finish before billing
        res1.join(); res2.join(); res3.join();

        // ────────────────────────────────────────────────────────────────────
        // STEP 5 — ALL THREE: Transaction with Discount + Payment
        // TransactionService calls user.getDiscount() (Member 1)
        // and PaymentProcessor.processPayment() (Member 3)
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\n══ STEP 5: Billing — TransactionService (Member 3 calls Member 1) ══");

        TransactionService txService = new TransactionService();

        if (bob != null) {
            try {
                System.out.println("\n[Dine-in]  User: " + bob.getUsername()
                        + " | Raw amount: ₹250.0"
                        + " | After " + (int)(0.20*100) + "% VIP discount: ₹"
                        + bob.getDiscount(250.0));
                // Overload 1: Dine-in (User + amount)
                txService.processOrder(bob, 250.0);
            } catch (PaymentException e) {
                System.out.println("Payment failed: " + e.getMessage());
            }
        }

        if (charlie != null) {
            try {
                System.out.println("\n[Delivery] User: " + charlie.getUsername()
                        + " | Raw amount: ₹180.0 + ₹40 delivery fee");
                // Overload 2: Delivery (User + amount + address)
                txService.processOrder(charlie, 180.0, "42 MG Road, Pune");
            } catch (PaymentException e) {
                System.out.println("Payment failed: " + e.getMessage());
            }
        }

        // ────────────────────────────────────────────────────────────────────
        // STEP 6 — MEMBER 2: Validate item availability (exception demo)
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\n══ STEP 6: Menu Validation + Custom Exceptions (Member 2) ══");
        String[] testOrders = { "Butter Chicken", "Shawarma Wrap", "Dragon Roll" };

        for (String itemName : testOrders) {
            try {
                MenuItem item = menuManager.findAndValidate(itemName);
                System.out.println("✅ Available: " + item);
            } catch (RestaurantMenuException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }

        System.out.println("\n╔═════════════════════════════════════╗");
        System.out.println("║   All systems running correctly! ✔   ║");
        System.out.println("╚═════════════════════════════════════╝");
    }

    // ── Helper ───────────────────────────────────────────────────────────────
    private static void printUser(User u) {
        if (u == null) return;
        System.out.printf("Logged in: %-12s | Discount on ₹100: ₹%.0f%n",
                u.getUsername(), u.getDiscount(100.0));
    }
}
