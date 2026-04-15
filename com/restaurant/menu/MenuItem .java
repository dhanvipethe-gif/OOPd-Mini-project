package com.restaurant.menu;

import java.io.Serializable;

/**
 * Base class for every item on the menu.
 *
 * CONTRACT for Member 3 (TransactionService / OrderProcessor):
 *   - getQuantityLeft()  → check before ordering
 *   - reduceQuantity()   → call after a successful order
 *
 * CONTRACT for Member 1 (TransactionService calls user.getDiscount(amount)):
 *   - getPrice() → the raw price before discount
 */
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final double price;
    private int quantityLeft;        // daily stock limit
    private final String cuisine;    // "Indian", "Arabic", "Italian", etc.

    public MenuItem(String name, double price, int dailyLimit) {
        this(name, price, dailyLimit, "General");
    }

    public MenuItem(String name, double price, int dailyLimit, String cuisine) {
        this.name         = name;
        this.price        = price;
        this.quantityLeft = dailyLimit;
        this.cuisine      = cuisine;
    }

    // ── Contract methods (Member 3 calls these) ───────────────────────────────

    /** @return how many portions are still available today */
    public int getQuantityLeft() {
        return quantityLeft;
    }

    /**
     * Decreases stock by 1 after a successful order.
     * Throws OutOfStockException (Member 2's custom exception) if stock = 0.
     */
    public synchronized void reduceQuantity() throws OutOfStockException {
        if (quantityLeft <= 0) {
            throw new OutOfStockException(
                "Sorry! \"" + name + "\" is sold out for today.");
        }
        quantityLeft--;
    }

    // ── Standard getters ─────────────────────────────────────────────────────

    public String getName()    { return name; }
    public double getPrice()   { return price; }
    public String getCuisine() { return cuisine; }

    public boolean isAvailable() { return quantityLeft > 0; }

    @Override
    public String toString() {
        return String.format("%-22s ₹%-8.2f [%s] Stock: %d",
                name, price, cuisine, quantityLeft);
    }
}
