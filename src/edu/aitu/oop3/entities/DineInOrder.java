package edu.aitu.oop3.entities;
import java.time.OffsetDateTime;
public class DineInOrder extends BaseOrder {
    public DineInOrder(long customerId) {
        super(0, customerId, OrderStatus.ACTIVE, OffsetDateTime.now());
    }
}

