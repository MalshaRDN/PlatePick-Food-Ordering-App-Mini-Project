package com.example.platepick;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    // Singleton instance for the entire app
    private static CartManager instance;

    // List to store cart items
    private List<Meal> cartItems;

    // Private constructor
    private CartManager() {
        cartItems = new ArrayList<>();
    }

    // Method to get the single instance of CartManager
    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Method to add a new meal to the cart with quantity
    public void addToCart(Meal meal, int quantity) {
        // Check if meal already exists in cart
        for (Meal item : cartItems) {
            if (item.getName().equals(meal.getName())) {
                // Update quantity if meal exists
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Create a new meal with the specified quantity
        Meal cartMeal = new Meal(meal.getName(), meal.getPrice(), meal.getImageResId());
        cartMeal.setQuantity(quantity);
        cartItems.add(cartMeal);
    }

    // Method to add a new meal to the cart (without quantity parameter for backward compatibility)
    public void addToCart(Meal meal) {
        addToCart(meal, 1);
    }

    // Method to retrieve all items currently in the cart
    public List<Meal> getCartItems() {
        return cartItems;
    }
}