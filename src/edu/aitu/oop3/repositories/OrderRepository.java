package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderItem;
import edu.aitu.oop3.entities.OrderStatus;

import java.util.List;
import java.util.Optional;

    public interface OrderRepository extends Repository<Order> {
        List<OrderItem> getItemsByOrderId(long orderId);
        long createOrderWithItems(Order order, List<OrderItem> items);
    Optional<Order> findById(long id);
    void updateStatus(long orderId, OrderStatus newStatus);
    List<Order> findByStatus(OrderStatus status);
}
