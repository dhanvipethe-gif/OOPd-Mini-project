package com.restaurant.menu;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

// ─────────────────────────────────────────────────────────────────────────────
// Table.java  (inner static class exported for convenience)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Represents a physical table in the cafe.
 * Serializable so booking state can be persisted.
 */
class Table implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum TableStatus { AVAILABLE, OCCUPIED, RESERVED }

    private final int tableNumber;
    private final int capacity;
    private TableStatus status;
    private String occupiedBy;  // customer name

    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity    = capacity;
        this.status      = TableStatus.AVAILABLE;
    }

    public int getTableNumber()          { return tableNumber; }
    public int getCapacity()             { return capacity; }
    public TableStatus getStatus()       { return status; }
    public void setStatus(TableStatus s) { this.status = s; }
    public String getOccupiedBy()        { return occupiedBy; }
    public void setOccupiedBy(String n)  { this.occupiedBy = n; }
    public boolean isAvailable()         { return status == TableStatus.AVAILABLE; }

    @Override
    public String toString() {
        return String.format("Table#%d [cap:%d | %s%s]",
                tableNumber, capacity, status,
                occupiedBy != null ? " | " + occupiedBy : "");
    }
}
