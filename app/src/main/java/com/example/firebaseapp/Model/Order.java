package com.example.firebaseapp.Model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    private String userId;
    private List<Product> products;
    private double totalAmount;
    private String status;
    private long timestamp;

    public Order() {
    }

    public Order(String userId, List<Product> products, double totalAmount, String status, long timestamp) {
        this.userId = userId;
        this.products = products;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
