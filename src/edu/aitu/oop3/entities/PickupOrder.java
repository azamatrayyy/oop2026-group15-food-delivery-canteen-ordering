package edu.aitu.oop3.entities;

import java.time.OffsetDateTime;

public class PickupOrder extends BaseOrder {
    public PickupOrder(long customerId) {
        super(0, customerId, OrderStatus.ACTIVE, OffsetDateTime.now());
    }
}

