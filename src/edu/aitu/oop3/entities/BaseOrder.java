package edu.aitu.oop3.entities;

import java.time.OffsetDateTime;

public abstract class BaseOrder extends Order {
    protected BaseOrder(long id, long customerId, OrderStatus status, OffsetDateTime createdAt) {
        super(id, customerId, status, createdAt);
    }
}