package edu.aitu.oop3.entities;

import java.math.BigDecimal;

public class OrderItem {
    private long id;
    private long orderId;
    private long menuItemId;
    private int quantity;
    private BigDecimal priceAtOrder; // can be null before saving

    public OrderItem(long id, long orderId, long menuItemId, int quantity, BigDecimal priceAtOrder) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public long getId() { return id; }
    public long getOrderId() { return orderId; }
    public long getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPriceAtOrder() { return priceAtOrder; }

    public void setPriceAtOrder(BigDecimal priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }
}
