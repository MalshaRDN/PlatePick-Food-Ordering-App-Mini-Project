package com.example.platepick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    // List to hold all the meals
    private List<Meal> mealList;

    // Constructor for the Adapter
    public MenuAdapter(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

        // Get the current meal object from the list
        Meal meal = mealList.get(position);

        // Set meal name, price, and image to the UI elements
        holder.tvMealName.setText(meal.getName());
        holder.tvMealPrice.setText(meal.getPrice());
        holder.imgMeal.setImageResource(meal.getImageResId());

        // Set click listener on the entire card item
        holder.itemView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                // Add the selected meal to the CartManager
                CartManager.getInstance().addToCart(meal);

                // Show a Toast message to confirm addition
                android.widget.Toast.makeText(v.getContext(), meal.getName() + " Added to Cart!", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return mealList.size();
    }

    // ViewHolder class to hold the UI views
    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvMealName, tvMealPrice;
        ImageView imgMeal;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link the IDs from menu_item.xml
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealPrice = itemView.findViewById(R.id.tvMealPrice);
            imgMeal = itemView.findViewById(R.id.imgMeal);
        }
    }
}
