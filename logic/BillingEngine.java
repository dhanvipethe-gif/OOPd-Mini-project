package logic;

import model.MenuItem;
import model.MenuItem;
import java.util.List;
import java.util.Map;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  OOPD Concept #2 — METHOD OVERLOADING                           ║
 * ║  Three overloads of calculateBill() share the same name but     ║
 * ║  differ in parameter lists, demonstrating compile-time          ║
 * ║  polymorphism (static dispatch).                                ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class BillingEngine {

    // ── Constants ─────────────────────────────────────────────────────────────
    public static final double GST_RATE          = 0.05;   // 5% GST
    public static final double VIP_DISCOUNT_RATE = 0.20;   // 20% off
    public static final int    POINTS_PER_RUPEE  = 10;     // 10 pts = ₹1

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static BillingEngine instance;
    private BillingEngine() {}
    public static BillingEngine getInstance() {
        if (instance == null) instance = new BillingEngine();
        return instance;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  OVERLOAD 1 — Standard Bill (no discount)
    //  Use case: Walk-in customer, no code, no membership
    // ═════════════════════════════════════════════════════════════════════════
    /**
     * Calculates a standard bill with GST only.
     *
     * @param items List of ordered MenuItems
     * @return      BillSummary with subtotal, tax, and grand total
     */
    public BillSummary calculateBill(List<MenuItem> items) {
        double subtotal = computeSubtotal(items);
        double tax      = subtotal * GST_RATE;
        double total    = subtotal + tax;

        return new BillSummary(subtotal, 0.0, tax, total, "Standard");
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  OVERLOAD 2 — VIP Code Discount
    //  Use case: Customer presents a promotional / VIP voucher code
    // ═════════════════════════════════════════════════════════════════════════
    /**
     * Applies a percentage discount identified by a VIP/promo code,
     * then adds GST on the discounted amount.
     *
     * @param items    List of ordered MenuItems
     * @param vipCode  Promotional code string (validated against registry)
     * @return         BillSummary with discount line shown separately
     */
    public BillSummary calculateBill(List<MenuItem> items, String vipCode) {
        double subtotal     = computeSubtotal(items);
        double discountRate = resolveVipDiscount(vipCode); // 0.0 if invalid
        double discount     = subtotal * discountRate;
        double afterDisc    = subtotal - discount;
        double tax          = afterDisc * GST_RATE;
        double total        = afterDisc + tax;

        String tag = discountRate > 0
                ? "VIP (" + vipCode + ", -" + (int)(discountRate*100) + "%)"
                : "VIP code invalid — Standard";

        return new BillSummary(subtotal, discount, tax, total, tag);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  OVERLOAD 3 — Membership Points Redemption
    //  Use case: Loyalty member burns accumulated points for cash-equivalent
    // ═════════════════════════════════════════════════════════════════════════
    /**
     * Redeems membership points as a bill credit before GST is applied.
     * Redemption: {@value #POINTS_PER_RUPEE} points = ₹1 discount.
     *
     * @param items          List of ordered MenuItems
     * @param memberName     Display name for the receipt
     * @param pointsToRedeem Points the member wishes to burn (≥ 0)
     * @return               BillSummary showing points redeemed and ₹ credit
     */
    public BillSummary calculateBill(List<MenuItem> items,
                                     String memberName,
                                     int pointsToRedeem) {
        double subtotal    = computeSubtotal(items);
        double pointCredit = Math.min(pointsToRedeem / (double) POINTS_PER_RUPEE,
                                      subtotal);          // can't exceed subtotal
        double afterCredit = subtotal - pointCredit;
        double tax         = afterCredit * GST_RATE;
        double total       = afterCredit + tax;

        String tag = String.format("Member: %s | %d pts redeemed (₹%.2f credit)",
                                   memberName, pointsToRedeem, pointCredit);

        return new BillSummary(subtotal, pointCredit, tax, total, tag);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private double computeSubtotal(List<MenuItem> items) {
        return items.stream()
                    .mapToDouble(MenuItem::getEffectivePrice)
                    .sum();
    }

    /** Simple registry of valid VIP codes → discount rates. */
    private static final Map<String, Double> VIP_CODE_REGISTRY = Map.of(
            "VVIP2024", 0.20,
            "STAFF10",  0.10,
            "LAUNCH25", 0.25
    );

    private double resolveVipDiscount(String code) {
        if (code == null) return 0.0;
        return VIP_CODE_REGISTRY.getOrDefault(code.toUpperCase(), 0.0);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Inner record — immutable bill result (Java 16+; use class if older)
    // ════════════════════════════════════════════════════════════════════════
    public static class BillSummary {
        public final double subtotal;
        public final double discount;
        public final double tax;
        public final double grandTotal;
        public final String discountTag;

        public BillSummary(double subtotal, double discount,
                           double tax, double grandTotal, String discountTag) {
            this.subtotal    = subtotal;
            this.discount    = discount;
            this.tax         = tax;
            this.grandTotal  = grandTotal;
            this.discountTag = discountTag;
        }

        @Override
        public String toString() {
            return String.format(
                "┌─────────────────────────────────┐%n" +
                "│           BILL SUMMARY          │%n" +
                "├─────────────────────────────────┤%n" +
                "│ Subtotal  :  ₹%9.2f          │%n" +
                "│ Discount  : -₹%9.2f  %-8s│%n" +
                "│ GST (5%%) :  ₹%9.2f          │%n" +
                "├─────────────────────────────────┤%n" +
                "│ GRAND TOTAL: ₹%9.2f          │%n" +
                "│ %-32s│%n" +
                "└─────────────────────────────────┘",
                subtotal, discount, "", tax, grandTotal, discountTag
            );
        }
    }
}
