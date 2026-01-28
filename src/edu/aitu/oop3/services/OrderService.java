package edu.aitu.oop3.services;

import edu.aitu.oop3.config.TaxConfig;
import edu.aitu.oop3.entities.*;
import edu.aitu.oop3.exceptions.InvalidQuantityException;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.aitu.oop3.services.pricing.PricingRules;
import edu.aitu.oop3.util.Result;
import edu.aitu.oop3.pricing.PricingRules;
import edu.aitu.oop3.util.Result; // если у вас Result<T> в другом пакете — поправь import
public class OrderService extends BaseService{
    private final OrderRepository orderRepo;
    private final MenuService menuService;
    private final PaymentService paymentService;
    long customerId;
    Order order = OrderFactory.create("pickup", customerId);
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
            double tax = total.doubleValue() * TaxConfig.getInstance().getTaxRate();
            total = total.add(BigDecimal.valueOf(tax));

        }

        paymentService.pay(total);

        PricingRules pricing = PricingRules.getInstance();
        BigDecimal finalTotal = pricing.calculateTotal(total);

        Order order = new Order.Builder()
                .customerId(customerId)
                .build();

        paymentService.pay(finalTotal);
        BigDecimal totalWithTax =
                TaxConfig.getInstance().applyTax(total);

        paymentService.pay(totalWithTax);

        return orderRepo.createOrderWithItems(order, itemsToSave);
    }
    public Result<BigDecimal> previewTotal(String orderType, List<OrderItem> itemsInput) {
        try {
            if (itemsInput == null || itemsInput.isEmpty()) {
                return Result.fail("Order must contain at least 1 item.");
            }

            BigDecimal subtotal = itemsInput.stream()
                    .map(it -> {
                        if (it.getQuantity() <= 0) {
                            throw new InvalidQuantityException("Invalid quantity for menuItemId=" + it.getMenuItemId());
                        }
                        MenuItem mi = menuService.getAvailableMenuItemOrThrow(it.getMenuItemId());
                        it.setPriceAtOrder(mi.getPrice());
                        return mi.getPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal total = PricingRules.getInstance().finalTotal(orderType, subtotal);
            return Result.ok(total);

        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
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