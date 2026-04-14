package com.restaurant.menu;



import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  OOPD Concept #1 — GENERICS with Bounded Type Parameter         ║
 * ║  <T extends MenuItem> guarantees compile-time type safety:      ║
 * ║  only MenuItem and its subclasses can be stored here.           ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * A type-safe container that manages one category of menu items.
 * Instantiate one per category: MenuManager<MenuItem>,
 *                                MenuManager<SpecialtyItem>, etc.
 *
 * @param <T> Any class that IS-A MenuItem (bounded upper wildcard)
 */
public class MenuManager<T extends MenuItem> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String categoryLabel;
    private final List<T> items = new ArrayList<>();

    public MenuManager(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    /** Add a new item to this category. */
    public void addItem(T item) {
        Objects.requireNonNull(item, "Item must not be null");
        items.add(item);
    }

    /** Remove item by its ID. Returns true if found and removed. */
    public boolean removeItem(String itemId) {
        return items.removeIf(i -> i.getItemId().equalsIgnoreCase(itemId));
    }

    /**
     * Retrieve item by ID.
     * Returns an Optional — callers must handle the "not found" case explicitly.
     */
    public Optional<T> findById(String itemId) {
        return items.stream()
                    .filter(i -> i.getItemId().equalsIgnoreCase(itemId))
                    .findFirst();
    }

    /** Return only items that are currently available. */
    public List<T> getAvailableItems() {
        return items.stream()
                    .filter(MenuItem::isAvailable)
                    .collect(Collectors.toList());
    }

    /** Return an unmodifiable view of ALL items. */
    public List<T> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    // ── Generic utility method ────────────────────────────────────────────────

    /**
     * Demonstrates a bounded generic method:
     * Finds the most expensive available item in THIS manager's list.
     * The wildcard <? extends MenuItem> lets this work for any subtype.
     */
    public Optional<T> getMostExpensiveAvailable() {
        return items.stream()
                    .filter(MenuItem::isAvailable)
                    .max(Comparator.comparingDouble(MenuItem::getEffectivePrice));
    }

    /** Total value of all available items on this menu section. */
    public double getTotalMenuValue() {
        return items.stream()
                    .filter(MenuItem::isAvailable)
                    .mapToDouble(MenuItem::getEffectivePrice)
                    .sum();
    }

    // ── Serialization helpers ────────────────────────────────────────────────

    /**
     * Persist this manager's data to a .dat file.
     * Called by CafeDataStore for the save-state feature.
     */
    public void saveToDisk(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    /**
     * Load a previously serialized MenuManager from disk.
     * Suppressed unchecked cast is safe because we control the serialization.
     */
    @SuppressWarnings("unchecked")
    public static <T extends MenuItem> MenuManager<T> loadFromDisk(String filePath)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            return (MenuManager<T>) ois.readObject();
        }
    }

    // ── Display ───────────────────────────────────────────────────────────────

    public String getCategoryLabel() { return categoryLabel; }

    public void printMenu() {
        System.out.println("\n═══ " + categoryLabel.toUpperCase() + " ═══");
        if (items.isEmpty()) {
            System.out.println("  (no items)");
        } else {
            items.forEach(i -> System.out.println("  " + i));
        }
    }

    @Override
    public String toString() {
        return "MenuManager[" + categoryLabel + ", items=" + items.size() + "]";
    }
}
