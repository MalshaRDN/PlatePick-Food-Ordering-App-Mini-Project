package com.example.platepick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> meals;
    private OnQuantityChangeListener quantityChangeListener;

    public MealAdapter(List<Meal> meals) {
        this.meals = meals;
    }

    public MealAdapter(List<Meal> meals, OnQuantityChangeListener listener) {
        this.meals = meals;
        this.quantityChangeListener = listener;
    }

    public void setQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.tvMealName.setText(meal.getName());
        holder.tvMealPrice.setText("Rs. " + meal.getPrice());
        holder.imgMeal.setImageResource(meal.getImageResId());

        // Initialize quantity to meal's quantity
        holder.tvQuantity.setText(String.valueOf(meal.getQuantity()));

        // Handle minus button
        holder.btnMinus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                holder.tvQuantity.setText(String.valueOf(quantity));
                meal.setQuantity(quantity);

                // Notify listener about quantity change
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(meal, quantity);
                }
            }
        });

        // Handle plus button
        holder.btnPlus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            quantity++;
            holder.tvQuantity.setText(String.valueOf(quantity));
            meal.setQuantity(quantity);

            // Notify listener about quantity change
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged(meal, quantity);
            }
        });

        // Handle Add to Cart button
        holder.btnAddToCart.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            CartManager.getInstance().addToCart(meal, quantity);

            // Reset quantity to 1 after adding to cart
            meal.setQuantity(1);
            holder.tvQuantity.setText("1");
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMeal;
        TextView tvMealName;
        TextView tvMealPrice;
        TextView tvQuantity;
        Button btnMinus;
        Button btnPlus;
        Button btnAddToCart;

        public MealViewHolder(View itemView) {
            super(itemView);
            imgMeal = itemView.findViewById(R.id.imgMeal);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealPrice = itemView.findViewById(R.id.tvMealPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    // Interface for quantity change callback
    public interface OnQuantityChangeListener {
        void onQuantityChanged(Meal meal, int newQuantity);
    }
}






