package com.restaurant.menu;

import model.MenuItem;
import model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  OOPD Concept #5 — SERIALIZATION                                ║
 * ║  Saves/loads the complete cafe state (menus + bookings) to a    ║
 * ║  single .dat file using Java Object Serialization.              ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class CafeDataStore {

    private static final String DEFAULT_FILE = "cafe_state.dat";

    // ── Serializable snapshot of all runtime state ────────────────────────────
    public static class CafeSnapshot implements Serializable {
        private static final long serialVersionUID = 1L;

        public final List<MenuManager<? extends MenuItem>> menuManagers;
        public final List<Order>    orders;
        public final int            totalTables;
        public final int            availableTables;
        public final List<String>   waitingList;

        public CafeSnapshot(List<MenuManager<? extends MenuItem>> menuManagers,
                            List<Order> orders,
                            int totalTables,
                            int availableTables,
                            List<String> waitingList) {
            this.menuManagers    = new ArrayList<>(menuManagers);
            this.orders          = new ArrayList<>(orders);
            this.totalTables     = totalTables;
            this.availableTables = availableTables;
            this.waitingList     = new ArrayList<>(waitingList);
        }
    }

    /** Persist the entire cafe state. */
    public static void save(CafeSnapshot snapshot) throws IOException {
        save(snapshot, DEFAULT_FILE);
    }

    public static void save(CafeSnapshot snapshot, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {
            oos.writeObject(snapshot);
            System.out.println("[CafeDataStore] State saved → " + filePath);
        }
    }

    /** Load a previously saved state. Returns null if file doesn't exist. */
    public static CafeSnapshot load() throws IOException, ClassNotFoundException {
        return load(DEFAULT_FILE);
    }

    public static CafeSnapshot load(String filePath)
            throws IOException, ClassNotFoundException {
        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("[CafeDataStore] No save file found — starting fresh.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(filePath)))) {
            CafeSnapshot snap = (CafeSnapshot) ois.readObject();
            System.out.println("[CafeDataStore] State loaded ← " + filePath);
            return snap;
        }
    }
}
