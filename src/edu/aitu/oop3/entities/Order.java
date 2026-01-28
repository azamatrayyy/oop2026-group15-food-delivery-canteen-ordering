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
    protected Order(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
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

    public static class Builder {
        private long id = 0;
        private long customerId;
        private OrderStatus status = OrderStatus.ACTIVE;
        private OffsetDateTime createdAt = OffsetDateTime.now();

        public Builder customerId(long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
