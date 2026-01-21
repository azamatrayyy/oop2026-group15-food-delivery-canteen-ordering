package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderItem;
import edu.aitu.oop3.entities.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    long createOrderWithItems(Order order, List<OrderItem> items);
    List<Order> findByStatus(OrderStatus status);
    Optional<Order> findById(long id);
    void updateStatus(long orderId, OrderStatus newStatus);
}
