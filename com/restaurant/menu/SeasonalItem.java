package com.restaurant.menu;

import java.time.Month;

/**
 * Level 3 of Multilevel Inheritance: MenuItem → SpecialtyItem → SeasonalItem
 * Applies a percentage premium on top of the specialty surcharge.
 * (Owned by Partner C — provided here as the integration contract.)
 */
public class SeasonalItem extends SpecialtyItem {
    private static final long serialVersionUID = 1L;

    private Month availableMonth;
    private double seasonalPremiumPercent; // e.g. 15.0 = 15%

    public SeasonalItem(String itemId, String name, double basePrice,
                        String category, String chefNote, int prepTimeMinutes,
                        Month availableMonth, double seasonalPremiumPercent) {
        super(itemId, name, basePrice, category, chefNote, prepTimeMinutes);
        this.availableMonth          = availableMonth;
        this.seasonalPremiumPercent  = seasonalPremiumPercent;
    }

    public Month getAvailableMonth()        { return availableMonth; }
    public double getSeasonalPremiumPercent() { return seasonalPremiumPercent; }

    @Override
    public double getEffectivePrice() {
        double specialtyPrice = super.getEffectivePrice();
        return specialtyPrice * (1 + seasonalPremiumPercent / 100.0);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Season: %s (+%.0f%%)",
                availableMonth, seasonalPremiumPercent);
    }
}
