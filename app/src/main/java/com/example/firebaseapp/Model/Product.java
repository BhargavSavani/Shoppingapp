package com.example.firebaseapp.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String title;
    private String imageUrl;
    private double price;
    private String description;
    private float rating;
    private int quantity;
    private boolean favorite;

    public Product() {
    }

    public Product(String id, String title, String imageUrl, double price, String description, float rating, int quantity) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.quantity = quantity;
    }


    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
