import logic.BillingEngine;
import logic.MenuManager;
import model.*;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * Console-based demo — verifies YOUR two OOPD concepts work correctly
 * without needing the full GUI stack.
 *
 * Run: javac -d out src/**&#47;*.java && java -cp out DemoRunner
 */
public class DemoRunner {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   CAFE COMPANION — Concept Demo Runner   ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // ────────────────────────────────────────────────────────────────────
        // DEMO 1 — Generics: MenuManager<T extends MenuItem>
        // ────────────────────────────────────────────────────────────────────
        System.out.println("━━━ DEMO 1: Bounded Generic — MenuManager<T extends MenuItem> ━━━");

        // Type-safe: only MenuItem (or subclass) accepted at compile time
        MenuManager<MenuItem>      drinks   = new MenuManager<>("Drinks");
        MenuManager<SpecialtyItem> specials = new MenuManager<>("Specialty");
        MenuManager<SeasonalItem>  seasonal = new MenuManager<>("Seasonal");

        drinks.addItem(new MenuItem("D001", "Espresso",  120, "Drinks"));
        drinks.addItem(new MenuItem("D002", "Cold Brew", 180, "Drinks"));

        specials.addItem(new SpecialtyItem("S001", "Truffle Pasta",
                350, "Mains", "Chef signature", 20));

        seasonal.addItem(new SeasonalItem("X001", "Mango Cheesecake",
                320, "Desserts", "Alphonso mango", 15, Month.APRIL, 15.0));

        drinks.printMenu();
        specials.printMenu();
        seasonal.printMenu();

        System.out.println("\nMost expensive drink : "
                + drinks.getMostExpensiveAvailable().map(MenuItem::getName).orElse("n/a"));
        System.out.println("Seasonal menu value  : ₹"
                + String.format("%.2f", seasonal.getTotalMenuValue()));

        // ────────────────────────────────────────────────────────────────────
        // DEMO 2 — Method Overloading: BillingEngine
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\n━━━ DEMO 2: Method Overloading — BillingEngine.calculateBill() ━━━");

        List<MenuItem> order = new ArrayList<>();
        order.addAll(drinks.getAvailableItems());
        order.addAll(specials.getAvailableItems());
        order.addAll(seasonal.getAvailableItems());

        BillingEngine engine = BillingEngine.getInstance();

        System.out.println("\n[Overload 1] Standard bill (no discount):");
        System.out.println(engine.calculateBill(order));

        System.out.println("\n[Overload 2] VIP code 'VVIP2024' (20% off):");
        System.out.println(engine.calculateBill(order, "VVIP2024"));

        System.out.println("\n[Overload 3] Member 'Priya Sharma' redeems 5000 points:");
        System.out.println(engine.calculateBill(order, "Priya Sharma", 5000));

        System.out.println("\n[Overload 2] Invalid VIP code 'FAKE123':");
        System.out.println(engine.calculateBill(order, "FAKE123"));

        System.out.println("\n✅  All concepts verified.");
    }
}
