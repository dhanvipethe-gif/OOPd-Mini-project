package com.restaurant.menu;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  OOPD Concept — GENERICS with Bounded Type Parameter            ║
 * ║  Menu<T extends MenuItem> is type-safe: you can only store      ║
 * ║  MenuItem and its subclasses (DailySpecial, etc.)               ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Usage examples:
 *   Menu<MenuItem>     indianMenu  = new Menu<>("Indian",  DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
 *   Menu<DailySpecial> weekendMenu = new Menu<>("Weekend Specials", DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
 *
 * @param <T> Any class that IS-A MenuItem
 */
public class Menu<T extends MenuItem> {

    private final String   cuisineName;
    private final List<T>  items = new ArrayList<>();

    // Days this cuisine section is served (inclusive range)
    private final DayOfWeek servedFrom;
    private final DayOfWeek servedUntil;

    public Menu(String cuisineName, DayOfWeek servedFrom, DayOfWeek servedUntil) {
        this.cuisineName  = cuisineName;
        this.servedFrom   = servedFrom;
        this.servedUntil  = servedUntil;
    }

    /** Add an item to this menu section. */
    public void addItem(T item) {
        items.add(item);
    }

    // ── Contract method for Member 3 (OrderProcessor calls this) ─────────────

    /**
     * Check if an item is available right now.
     * Throws MenuUnavailableException if today is outside the serving days.
     * Throws ItemNotFoundException if the name doesn't match anything.
     * Returns the item if found AND in stock.
     *
     * @param itemName  exact item name (case-insensitive)
     * @return the matched MenuItem
     */
    public T checkAvailability(String itemName)
            throws RestaurantMenuException {

        // 1. Is today a valid day for this cuisine section?
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (!isDayInRange(today)) {
            throw new MenuUnavailableException(cuisineName, today.toString());
        }

        // 2. Does the item exist?
        T found = items.stream()
                       .filter(i -> i.getName().equalsIgnoreCase(itemName))
                       .findFirst()
                       .orElseThrow(() -> new ItemNotFoundException(itemName));

        // 3. Is it still in stock?
        if (!found.isAvailable()) {
            throw new OutOfStockException(
                    "\"" + itemName + "\" is sold out for today.");
        }

        return found;
    }

    // ── Convenience methods ───────────────────────────────────────────────────

    /** Returns only items with stock remaining. */
    public List<T> getAvailableItems() {
        return items.stream()
                    .filter(MenuItem::isAvailable)
                    .collect(Collectors.toList());
    }

    public List<T> getAllItems()   { return new ArrayList<>(items); }
    public String  getCuisineName() { return cuisineName; }

    public boolean isServedToday() {
        return isDayInRange(LocalDate.now().getDayOfWeek());
    }

    // Checks if a day falls within the Monday→Friday style range
    private boolean isDayInRange(DayOfWeek day) {
        return day.getValue() >= servedFrom.getValue()
            && day.getValue() <= servedUntil.getValue();
    }

    /** Pretty-print the menu for console/debug use */
    public void printMenu() {
        System.out.println("\n╔══ " + cuisineName.toUpperCase() +
                " (Served: " + servedFrom + " – " + servedUntil + ") ══╗");
        if (items.isEmpty()) {
            System.out.println("  (no items)");
        } else {
            items.forEach(i -> System.out.println("  " + i));
        }
        System.out.println("╚" + "═".repeat(50) + "╝");
    }

    @Override
    public String toString() {
        return "Menu[" + cuisineName + ", items=" + items.size() + "]";
    }
}
