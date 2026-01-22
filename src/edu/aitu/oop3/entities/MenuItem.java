package edu.aitu.oop3.entities;

import java.math.BigDecimal;

public class MenuItem {
    private long id;
    private String name;
    private BigDecimal price;
    private boolean available;

    public MenuItem(long id, String name, BigDecimal price, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public boolean isAvailable() { return available; }

    @Override
    public String toString() {
        return "MenuItem{id=" + id + ", name='" + name + "', price=" + price + ", available=" + available + "}";
    }
}
