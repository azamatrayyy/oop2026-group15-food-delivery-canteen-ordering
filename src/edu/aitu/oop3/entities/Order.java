package edu.aitu.oop3.entities;

import java.time.OffsetDateTime;

public class Order {
    private long id;
    private long customerId;
    private OrderStatus status;
    private OffsetDateTime createdAt;
    public Order(long id, long customerId, OrderStatus status, OffsetDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public long getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Order{id=" + id + ", customerId=" + customerId +
                ", status=" + status + ", createdAt=" + createdAt + "}";
    }
}
