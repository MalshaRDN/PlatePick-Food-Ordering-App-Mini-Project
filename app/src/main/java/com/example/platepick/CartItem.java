package com.example.platepick;

public class CartItem {
    private Meal meal;
    private int quantity;

    public CartItem(Meal meal, int quantity) {
        this.meal = meal;
        this.quantity = quantity;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return Double.parseDouble(meal.getPrice()) * quantity;
    }
}

