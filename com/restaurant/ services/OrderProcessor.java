package com.restaurant.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderProcessor implements Runnable {
    private String customerName;
    private static int dailyOrderLimit = 5; // Shared limit

    public OrderProcessor(String name) {
        this.customerName = name;
    }

    @Override
    public void run() {
        //Synchronized ensures threads don't "over-order"
        synchronized (OrderProcessor.class) {
            if (dailyOrderLimit > 0) {
                System.out.println("[Thread " + Thread.currentThread().getId() + "] Processing " + customerName);
                dailyOrderLimit--;
                System.out.println("Orders left today: " + dailyOrderLimit);
            } else {
                System.out.println("FAILED: Limit reached for " + customerName);
            }
        }
    }

    //Method to run the pool
    public static void startOrderingSystem() {
        ExecutorService pool = Executors.newFixedThreadPool(3); //3 threads at a time
        
        String[] customers = {"VIP_Alex", "User_Sam", "Premium_John", "User_Sara", "VIP_Mia", "User_Zack"};
        
        for (String c : customers) {
            pool.execute(new OrderProcessor(c));
        }
        pool.shutdown();
    }
}
