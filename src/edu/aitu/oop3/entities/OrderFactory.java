package edu.aitu.oop3.entities;

public class OrderFactory {
    public static Order create(String type, long customerId) {
        return switch (type.toLowerCase()) {
            case "pickup" -> new PickupOrder(customerId);
            case "delivery" -> new DeliveryOrder(customerId);
            case "dinein" -> new DineInOrder(customerId);
            default -> throw new IllegalArgumentException("Unknown order type");
        };
    }
}
