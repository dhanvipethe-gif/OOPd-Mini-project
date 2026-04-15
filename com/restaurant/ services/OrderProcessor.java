package com.restaurant.services;

import com.restaurant.menu.MenuItem;           // Importing Member 2's work
import com.restaurant.menu.OutOfStockException; // Importing Member 2's exception
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Member 3 (Scientist Role): Multi-threaded Order Engine
 * This class demonstrates:
 * 1. Runnable Interface for threading
 * 2. Fine-grained Synchronization on specific objects
 * 3. Thread Pool management (ExecutorService)
 */
public class OrderProcessor implements Runnable {
    private String customerName;
    private MenuItem itemRequested;

    public OrderProcessor(String customerName, MenuItem itemRequested) {
        this.customerName = customerName;
        this.itemRequested = itemRequested;
    }

    @Override
    public void run() {
        // We synchronize on the specific ITEM, not the whole class.
        // This allows different items to be processed at the same time (higher efficiency).
        synchronized (itemRequested) {
            try {
                System.out.println("[Thread " + Thread.currentThread().getId() + 
                                   "] Processing " + customerName + "'s request for " + itemRequested.getName());
                
                // CALLING MEMBER 2'S CODE: reduceQuantity() handles the stock logic
                itemRequested.reduceQuantity(); 
                
                System.out.println("✅ SUCCESS: " + itemRequested.getName() + 
                                   " served to " + customerName + 
                                   ". (Stock left: " + itemRequested.getQuantityLeft() + ")");

            } catch (OutOfStockException e) {
                // CATCHING MEMBER 2'S EXCEPTION: handles the "Limit reached" logic
                System.out.println("❌ FAILED: " + e.getMessage() + " for customer " + customerName);
            }
        }
    }

    /**
     * Entry point to launch the multi-threaded ordering system.
     * Seeds the system with the real menu from Member 2.
     */
    public static void startOrderingSystem(List<MenuItem> dailyMenu) {
        if (dailyMenu.isEmpty()) {
            System.out.println("No items available today to process.");
            return;
        }

        // Creating a pool of 3 worker threads
        ExecutorService pool = Executors.newFixedThreadPool(3);
        
        // Simulation: Assigning different items to different customers
        String[] testCustomers = {"Alex", "Sam", "John", "Sara", "Mia", "Zack"};
        
        for (int i = 0; i < testCustomers.length; i++) {
            // We rotate through the menu items so different items are tested
            MenuItem selectedItem = dailyMenu.get(i % dailyMenu.size());
            
            // Launching the thread
            pool.execute(new OrderProcessor(testCustomers[i], selectedItem));
        }
        
        pool.shutdown(); // Stop accepting new tasks
    }
}
