package edu.aitu.oop3.entities;
import java.time.OffsetDateTime;
public class DeliveryOrder extends BaseOrder {
    public DeliveryOrder(long customerId) {
        super(0, customerId, OrderStatus.ACTIVE, OffsetDateTime.now());
    }
}

