package com.restaurant.menu;

import model.Order;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  OOPD Concepts #3 & #4 — MULTITHREADING (Partner A's Role)     ║
 * ║  This class is the INTEGRATION CONTRACT for Partner A.          ║
 * ║                                                                  ║
 * ║  Requirements:                                                   ║
 * ║  • bookTable() / releaseTable() must be synchronized            ║
 * ║    to prevent over-booking (intrinsic lock on `this`).          ║
 * ║  • waitingList add/notify must use wait()/notify() on the       ║
 * ║    shared capacity monitor object.                              ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * ── Partner A: flesh out the synchronized blocks below ──────────────
 */
public class BookingEngine implements Serializable {
    private static final long serialVersionUID = 1L;

    // Shared state — Partner A must guard ALL mutations with synchronized
    private final int totalTables;
    private int availableTables;
    private final Queue<String> waitingList = new LinkedList<>();

    // Thread-safe observer list so GUI (Partner C) gets live updates
    private final List<BookingListener> listeners = new CopyOnWriteArrayList<>();

    public BookingEngine(int totalTables) {
        this.totalTables     = totalTables;
        this.availableTables = totalTables;
    }

    // ── Contract methods (Partner A implements the bodies) ──────────────────

    /**
     * Attempt to book a table for a customer.
     * Must be synchronized. If no table is free, add customer to waitingList
     * and call wait() until a table becomes available (Inter-thread comm).
     *
     * @return true if booked immediately; false if added to waiting list
     */
    public synchronized boolean bookTable(String customerName) {
        // TODO (Partner A): implement synchronized booking + wait() logic
        if (availableTables > 0) {
            availableTables--;
            notifyListeners();
            return true;
        } else {
            waitingList.offer(customerName);
            return false;
        }
    }

    /**
     * Release a table (customer leaves). If waiting list is non-empty,
     * assign the table and call notify() to wake the waiting thread.
     */
    public synchronized void releaseTable(String customerName) {
        // TODO (Partner A): implement notify() for waiting list
        if (!waitingList.isEmpty()) {
            String next = waitingList.poll();
            // Table count stays same — transferred directly
            notifyListeners();
        } else {
            availableTables++;
            notifyListeners();
        }
    }

    // ── Getters (safe to call from GUI thread via EventDispatchThread) ───────
    public synchronized int getAvailableTables() { return availableTables; }
    public synchronized int getTotalTables()      { return totalTables; }
    public synchronized List<String> getWaitingList() {
        return new ArrayList<>(waitingList);
    }

    // ── Observer pattern — GUI subscribes to capacity changes ────────────────
    public void addListener(BookingListener l) { listeners.add(l); }
    private void notifyListeners() {
        listeners.forEach(l -> l.onCapacityChanged(availableTables, totalTables));
    }

    /** Functional interface for GUI (Partner C) to receive live seat updates. */
    @FunctionalInterface
    public interface BookingListener {
        void onCapacityChanged(int available, int total);
    }
}
