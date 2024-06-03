package com.example.firebaseapp.Model;

public class CartItem {
    private String productId;
    private String imageUrl;
    private String title;
    private double price;
    private String description;
    private float rating;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String productId, String imageUrl, String title, double price, String description, float rating, int quantity) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
