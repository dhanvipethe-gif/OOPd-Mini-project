package com.restaurant.menu;

/**
 * ══════════════════════════════════════════════════════
 *  Custom Exception Hierarchy  (Member 2's OOPD concept)
 *
 *  RestaurantMenuException
 *      └── OutOfStockException
 *      └── ItemNotFoundException
 *      └── MenuUnavailableException  (e.g. wrong day-of-week)
 * ══════════════════════════════════════════════════════
 *
 * Member 3 tip: catch RestaurantMenuException broadly, or
 * catch each subtype for specific error messages in the GUI.
 */

// ── Root ──────────────────────────────────────────────────────────────────────
class RestaurantMenuException extends Exception {
    public RestaurantMenuException(String message) { super(message); }
}

// ── Stock ran out today ───────────────────────────────────────────────────────
public class OutOfStockException extends RestaurantMenuException {
    public OutOfStockException(String message) { super(message); }
}

// ── Item name not found on the menu ──────────────────────────────────────────
class ItemNotFoundException extends RestaurantMenuException {
    public ItemNotFoundException(String itemName) {
        super("\"" + itemName + "\" was not found on today's menu.");
    }
}

// ── Menu section not available on this day ───────────────────────────────────
class MenuUnavailableException extends RestaurantMenuException {
    public MenuUnavailableException(String section, String day) {
        super("\"" + section + "\" is not served on " + day + ".");
    }
}
