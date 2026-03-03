package com.example.platepick;

public class Meal {
    // Variables to store meal details
    private String name;
    private String price;
    private int imageResId; // To store the image from drawable folder

    // Constructor
    public Meal(String name, String price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
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
}
