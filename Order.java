package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a customer's order (cart of MenuItems).
 * Used by both the GUI (Partner C) and BillingEngine (Your Role).
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum OrderStatus { PENDING, CONFIRMED, PREPARING, DELIVERED, CANCELLED }

    private final String orderId;
    private final String customerName;
    private final List<MenuItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private int tableNumber; // 0 = takeaway

    public Order(String customerName, int tableNumber) {
        this.orderId      = "ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.customerName = customerName;
        this.tableNumber  = tableNumber;
        this.items        = new ArrayList<>();
        this.status       = OrderStatus.PENDING;
        this.createdAt    = LocalDateTime.now();
    }

    public void addItem(MenuItem item)    { items.add(item); }
    public void removeItem(MenuItem item) { items.remove(item); }

    public String getOrderId()            { return orderId; }
    public String getCustomerName()       { return customerName; }
    public List<MenuItem> getItems()      { return Collections.unmodifiableList(items); }
    public OrderStatus getStatus()        { return status; }
    public void setStatus(OrderStatus s)  { this.status = s; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public int getTableNumber()           { return tableNumber; }

    public double getSubtotal() {
        return items.stream().mapToDouble(MenuItem::getEffectivePrice).sum();
    }

    @Override
    public String toString() {
        return String.format("Order[%s | %s | Table:%d | Items:%d | ₹%.2f | %s]",
                orderId, customerName, tableNumber, items.size(), getSubtotal(), status);
    }
}
