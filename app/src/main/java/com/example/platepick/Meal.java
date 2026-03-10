package com.example.platepick;

public class Meal {
    // Variables to store meal details
    private String name;
    private String price;
    private int imageResId; // To store the image from drawable folder
    private int quantity; // To store the quantity

    // Constructor
    public Meal(String name, String price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 1; // Default quantity is 1
    }

    // Getters to retrieve the data
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
