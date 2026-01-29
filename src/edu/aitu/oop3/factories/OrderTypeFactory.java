package edu.aitu.oop3.factories;

import edu.aitu.oop3.entities.orderType.*;

public class OrderTypeFactory {

    public static OrderType create(String type) {
        return switch (type.toUpperCase()) {
            case "DELIVERY" -> new DeliveryOrder();
            case "DINEIN" -> new DineInOrder();
            case "PICKUP"   -> new PickupOrder();
            default -> throw new IllegalArgumentException("Unknown order type: " + type);
        };
    }
}
