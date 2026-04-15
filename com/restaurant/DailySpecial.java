package com.restaurant.menu;

import java.time.DayOfWeek;

/**
 * A DailySpecial is a MenuItem that is only available on ONE specific day
 * and carries a discount percentage.
 *
 * Demonstrates: Inheritance (DailySpecial extends MenuItem)
 * and method overriding (getPrice returns discounted price).
 */
public class DailySpecial extends MenuItem {

    private final DayOfWeek availableOn;
    private final double    discountPercent;   // e.g. 15.0 = 15% off

    public DailySpecial(String name, double price, int dailyLimit,
                        String cuisine, DayOfWeek availableOn, double discountPercent) {
        super(name, price, dailyLimit, cuisine);
        this.availableOn     = availableOn;
        this.discountPercent = discountPercent;
    }

    /** Returns the discounted price when it's the right day, else full price. */
    @Override
    public double getPrice() {
        if (java.time.LocalDate.now().getDayOfWeek() == availableOn) {
            return super.getPrice() * (1.0 - discountPercent / 100.0);
        }
        return super.getPrice();
    }

    public DayOfWeek getAvailableOn()    { return availableOn; }
    public double getDiscountPercent()   { return discountPercent; }

    @Override
    public String toString() {
        return super.toString() + String.format(
                " | SPECIAL on %s (-%.0f%%)", availableOn, discountPercent);
    }
}
