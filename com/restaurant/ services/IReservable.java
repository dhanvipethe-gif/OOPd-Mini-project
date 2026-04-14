package com.restaurant.services;

/**
 * Interface for the Reservation System.
 * Demonstrates Abstraction for table management.
 */
public interface IReservable {
    boolean reserveTable(String name, int tableNumber);
    void releaseTable(int tableNumber);
}
