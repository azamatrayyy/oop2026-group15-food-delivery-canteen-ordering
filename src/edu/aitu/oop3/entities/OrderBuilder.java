package edu.aitu.oop3.entities;

import java.time.OffsetDateTime;

public class OrderBuilder {

    private long customerId;
    private OrderStatus status = OrderStatus.ACTIVE;

    public OrderBuilder customer(long customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public Order build() {
        return new Order(
                0,
                customerId,
                status,
                OffsetDateTime.now()
        );
    }
}
