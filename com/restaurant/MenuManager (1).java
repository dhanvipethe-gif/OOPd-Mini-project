package com.restaurant.menu;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * ══════════════════════════════════════════════════════
 *  MenuManager — the single entry point for Member 3.
 *
 *  Member 3 usage:
 *
 *    MenuManager mgr  = new MenuManager();
 *    List<MenuItem> allItems = mgr.getDailyMenu();
 *
 *    // Inside OrderProcessor / TransactionService:
 *    MenuItem item = mgr.findAndValidate("Butter Chicken");
 *    item.reduceQuantity();   // after payment succeeds
 * ══════════════════════════════════════════════════════
 */
public class MenuManager {

    // One Menu<T> per cuisine — generic containers
    private final Menu<MenuItem>     indianMenu;
    private final Menu<MenuItem>     arabicMenu;
    private final Menu<DailySpecial> weekendSpecials;

    public MenuManager() {
        // ── Indian (Mon–Sat) ──────────────────────────────────────────────────
        indianMenu = new Menu<>("Indian", DayOfWeek.MONDAY, DayOfWeek.SATURDAY);
        indianMenu.addItem(new MenuItem("Butter Chicken",  250.0, 10, "Indian"));
        indianMenu.addItem(new MenuItem("Paneer Tikka",    180.0,  8, "Indian"));
        indianMenu.addItem(new MenuItem("Dal Makhani",     150.0, 12, "Indian"));
        indianMenu.addItem(new MenuItem("Garlic Naan",      40.0, 30, "Indian"));

        // ── Arabic (Tue–Sun) ──────────────────────────────────────────────────
        arabicMenu = new Menu<>("Arabic", DayOfWeek.TUESDAY, DayOfWeek.SUNDAY);
        arabicMenu.addItem(new MenuItem("Shawarma Wrap",   200.0, 15, "Arabic"));
        arabicMenu.addItem(new MenuItem("Hummus Platter",  120.0, 10, "Arabic"));
        arabicMenu.addItem(new MenuItem("Falafel Bowl",    170.0,  8, "Arabic"));

        // ── Weekend Specials (Sat–Sun only) ───────────────────────────────────
        weekendSpecials = new Menu<>("Weekend Specials",
                DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        weekendSpecials.addItem(new DailySpecial(
                "Truffle Pasta", 350.0, 5, "Italian",
                DayOfWeek.SATURDAY, 20.0));
        weekendSpecials.addItem(new DailySpecial(
                "Avocado Brunch", 280.0, 6, "Continental",
                DayOfWeek.SUNDAY, 15.0));
    }

    // ── Contract method called by Member 3's OrderProcessor ──────────────────

    /**
     * Finds an item by name across ALL menu sections and validates it.
     * This is the ONLY method Member 3 needs to call before ordering.
     *
     * @param itemName  case-insensitive name
     * @return          the MenuItem if found, available, and in-season
     * @throws RestaurantMenuException  (OutOfStock / ItemNotFound / Unavailable)
     */
    public MenuItem findAndValidate(String itemName)
            throws RestaurantMenuException {

        // Try each cuisine section
        try { return indianMenu.checkAvailability(itemName); }
        catch (MenuUnavailableException | ItemNotFoundException ignored) {}

        try { return arabicMenu.checkAvailability(itemName); }
        catch (MenuUnavailableException | ItemNotFoundException ignored) {}

        try { return weekendSpecials.checkAvailability(itemName); }
        catch (MenuUnavailableException | ItemNotFoundException ignored) {}

        // Not found anywhere
        throw new ItemNotFoundException(itemName);
    }

    /**
     * Returns a flat list of ALL available items today.
     * Member 3's OrderProcessor seeds its dailyMenu from this.
     */
    public List<MenuItem> getDailyMenu() {
        List<MenuItem> all = new ArrayList<>();
        all.addAll(indianMenu.getAvailableItems());
        all.addAll(arabicMenu.getAvailableItems());
        all.addAll(weekendSpecials.getAvailableItems());
        return all;
    }

    /** Print all menus to console (useful for debugging / viva demo) */
    public void printAllMenus() {
        indianMenu.printMenu();
        arabicMenu.printMenu();
        weekendSpecials.printMenu();
    }

    // Expose individual menus if needed
    public Menu<MenuItem>     getIndianMenu()       { return indianMenu; }
    public Menu<MenuItem>     getArabicMenu()       { return arabicMenu; }
    public Menu<DailySpecial> getWeekendSpecials()  { return weekendSpecials; }
}
