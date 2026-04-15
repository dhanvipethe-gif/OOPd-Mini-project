package model;

/**
 * Level 2 of Multilevel Inheritance: MenuItem → SpecialtyItem
 * Represents premium/chef-special items with an added preparation time.
 * (Owned by Partner C — provided here as the integration contract.)
 */
public class SpecialtyItem extends MenuItem {
    private static final long serialVersionUID = 1L;

    private String chefNote;
    private int prepTimeMinutes;
    private static final double SPECIALTY_SURCHARGE = 20.0; // flat surcharge in ₹

    public SpecialtyItem(String itemId, String name, double basePrice,
                         String category, String chefNote, int prepTimeMinutes) {
        super(itemId, name, basePrice, category);
        this.chefNote       = chefNote;
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public String getChefNote()       { return chefNote; }
    public int getPrepTimeMinutes()   { return prepTimeMinutes; }

    @Override
    public double getEffectivePrice() {
        return super.getEffectivePrice() + SPECIALTY_SURCHARGE;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Chef: \"%s\" | Prep: %d min",
                chefNote, prepTimeMinutes);
    }
}
