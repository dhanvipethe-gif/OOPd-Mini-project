package model;

import java.io.Serializable;

/**
 * Base class for all menu items.
 * Partner C will extend this into SpecialtyItem -> SeasonalItem (Multilevel Inheritance).
 */
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemId;
    private String name;
    private double basePrice;
    private String category;
    private boolean available;

    public MenuItem(String itemId, String name, double basePrice, String category) {
        this.itemId    = itemId;
        this.name      = name;
        this.basePrice = basePrice;
        this.category  = category;
        this.available = true;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String getItemId()    { return itemId; }
    public String getName()      { return name; }
    public double getBasePrice() { return basePrice; }
    public String getCategory()  { return category; }
    public boolean isAvailable() { return available; }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setAvailable(boolean available) { this.available = available; }
    public void setBasePrice(double basePrice)  { this.basePrice = basePrice; }

    /**
     * Overridable: subclasses may apply surcharges (e.g. SeasonalItem premium).
     */
    public double getEffectivePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - ₹%.2f (%s)%s",
                itemId, name, getEffectivePrice(), category,
                available ? "" : " [UNAVAILABLE]");
    }
}
