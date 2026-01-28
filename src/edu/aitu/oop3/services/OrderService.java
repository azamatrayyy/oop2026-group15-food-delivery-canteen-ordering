package edu.aitu.oop3.services;

import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderItem;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.exceptions.InvalidQuantityException;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService extends BaseService{
    private final OrderRepository orderRepo;
    private final MenuService menuService;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepo, MenuService menuService, PaymentService paymentService) {
        this.orderRepo = orderRepo;
        this.menuService = menuService;
        this.paymentService = paymentService;
    }

    public long placeOrder(long customerId, List<OrderItem> itemsInput) {
        log("Placing order for customer " + customerId);
        if (itemsInput == null || itemsInput.isEmpty()) {
            throw new InvalidQuantityException("Order must contain at least 1 item.");
        }

        List<OrderItem> itemsToSave = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem it : itemsInput) {
            if (it.getQuantity() <= 0) {
                throw new InvalidQuantityException("Invalid quantity for menuItemId=" + it.getMenuItemId());
            }

            MenuItem menuItem = menuService.getAvailableMenuItemOrThrow(it.getMenuItemId());
            it.setPriceAtOrder(menuItem.getPrice());

            total = total.add(menuItem.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
            itemsToSave.add(it);
        }

        paymentService.pay(total);

        Order order = new Order(0, customerId, OrderStatus.ACTIVE, OffsetDateTime.now());
        return orderRepo.createOrderWithItems(order, itemsToSave);
    }

    public List<Order> getActiveOrders() {
        return orderRepo.findByStatus(OrderStatus.ACTIVE);
    }

    public void markCompleted(long orderId) {
        orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        orderRepo.updateStatus(orderId, OrderStatus.COMPLETED);
    }
}