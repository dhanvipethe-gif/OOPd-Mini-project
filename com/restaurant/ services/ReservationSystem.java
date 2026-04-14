package com.restaurant.services;

public class ReservationSystem implements Runnable, IReservable {
    private String customerName;
    private int requestedTable;
    // Shared resource: 10 tables (false = empty, true = occupied)
    private static boolean[] tables = new boolean[10]; 

    public ReservationSystem(String name, int tableNum) {
        this.customerName = name;
        this.requestedTable = tableNum;
    }

    @Override
    public void run() {
        // Synchronized prevents two threads from booking the same table
        synchronized (tables) {
            if (requestedTable < tables.length && !tables[requestedTable]) {
                reserveTable(customerName, requestedTable);
            } else {
                System.out.println("Reservation FAILED for " + customerName + ": Table " + requestedTable + " is taken.");
            }
        }
    }

    @Override
    public boolean reserveTable(String name, int tableNum) {
        tables[tableNum] = true;
        System.out.println("SUCCESS: Table " + tableNum + " reserved for " + name);
        return true;
    }

    @Override
    public void releaseTable(int tableNum) {
        tables[tableNum] = false;
    }
}
