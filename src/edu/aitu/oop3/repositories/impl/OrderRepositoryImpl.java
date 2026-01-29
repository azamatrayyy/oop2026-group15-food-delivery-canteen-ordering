package edu.aitu.oop3.repositories.impl;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderItem;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.repositories.OrderRepository;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.entities.OrderItem;

public class OrderRepositoryImpl implements OrderRepository {
    private final IDB db;

    public OrderRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public long createOrderWithItems(Order order, List<OrderItem> items) {
        String insertOrder = "insert into orders(customer_id, status) values (?, ?) returning id";
        String insertItem = "insert into order_items(order_id, menu_item_id, quantity, price_at_order) values (?, ?, ?, ?)";

        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);

            long orderId;
            try (PreparedStatement st = con.prepareStatement(insertOrder)) {
                st.setLong(1, order.getCustomerId());
                st.setString(2, order.getStatus().name());
                try (ResultSet rs = st.executeQuery()) {
                    rs.next();
                    orderId = rs.getLong(1);
                }
            }

            try (PreparedStatement st = con.prepareStatement(insertItem)) {
                for (OrderItem it : items) {
                    st.setLong(1, orderId);
                    st.setLong(2, it.getMenuItemId());
                    st.setInt(3, it.getQuantity());
                    st.setBigDecimal(4, it.getPriceAtOrder());
                    st.addBatch();
                }
                st.executeBatch();
            }

            con.commit();
            con.setAutoCommit(true);
            return orderId;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while creating order: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        String sql = "select id, customer_id, status, created_at from orders where status = ? order by id desc";
        List<Order> list = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, status.name());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                            rs.getLong("id"),
                            rs.getLong("customer_id"),
                            OrderStatus.valueOf(rs.getString("status")),
                            rs.getObject("created_at", OffsetDateTime.class)
                    ));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    @Override
    public Order save(Order entity) {
        return null;
    }

    @Override
    public Optional<Order> findById(long id) {
        String sql = "select id, customer_id, status, created_at from orders where id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                return Optional.of(new Order(
                        rs.getLong("id"),
                        rs.getLong("customer_id"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getObject("created_at", OffsetDateTime.class)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateStatus(long orderId, OrderStatus newStatus) {
        String sql = "update orders set status = ? where id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, newStatus.name());
            st.setLong(2, orderId);

            int updated = st.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Order not found in DB for update: " + orderId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }
    @Override
    public List<Order> findAll() {
        String sql = "SELECT id, customer_id, status, created_at FROM orders ORDER BY id DESC";
        List<Order> list = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(new Order(
                        rs.getLong("id"),
                        rs.getLong("customer_id"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getObject("created_at", OffsetDateTime.class)
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error in findAll: " + e.getMessage(), e);
        }
    }
    @Override
    public List<OrderItem> getItemsByOrderId(long orderId) {

        String sql = "select * from order_items where order_id = ?";
        List<OrderItem> list = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, orderId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderItem(
                            rs.getLong("id"),
                            rs.getLong("order_id"),
                            rs.getLong("menu_item_id"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("price_at_order")
                    ));
                }
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

}
