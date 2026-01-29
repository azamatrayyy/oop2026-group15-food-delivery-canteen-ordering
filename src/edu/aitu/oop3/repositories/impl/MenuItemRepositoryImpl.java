package edu.aitu.oop3.repositories.impl;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.repositories.MenuItemRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemRepositoryImpl implements MenuItemRepository {
    private final IDB db;

    public MenuItemRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public List<MenuItem> findAll() {
        String sql = "select id, name, price, available from menu_items order by id";
        List<MenuItem> list = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(new MenuItem(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getBoolean("available")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<MenuItem> findById(long id) {
        String sql = "select id, name, price, available from menu_items where id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new MenuItem(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getBoolean("available")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }
}