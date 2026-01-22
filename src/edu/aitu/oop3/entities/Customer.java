package edu.aitu.oop3.entities;

public class Customer {
    private long id;
    private String name;
    private String phone;

    public Customer(long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name='" + name + "', phone='" + phone + "'}";
    }
}
