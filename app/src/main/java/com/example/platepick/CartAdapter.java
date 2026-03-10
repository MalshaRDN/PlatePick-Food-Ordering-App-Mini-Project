package com.example.platepick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    // List to hold items added to the cart
    private List<Meal> cartList;
    private OnCartChangeListener cartChangeListener;

    // Constructor
    public CartAdapter(List<Meal> cartList) {
        this.cartList = cartList;
    }

    public CartAdapter(List<Meal> cartList, OnCartChangeListener listener) {
        this.cartList = cartList;
        this.cartChangeListener = listener;
    }

    public void setCartChangeListener(OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the cart_item.xml layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Get the current meal
        Meal meal = cartList.get(position);

        // Set data to views
        holder.tvCartMealName.setText(meal.getName());
        holder.tvCartMealPrice.setText("Rs. " + meal.getPrice());
        holder.imgCartMeal.setImageResource(meal.getImageResId());
        holder.tvCartQuantity.setText(String.valueOf(meal.getQuantity()));

        // Handle minus button
        holder.btnCartMinus.setOnClickListener(v -> {
            int quantity = meal.getQuantity();
            if (quantity > 1) {
                quantity--;
                meal.setQuantity(quantity);
                holder.tvCartQuantity.setText(String.valueOf(quantity));

                // Notify listener about cart change to update total
                if (cartChangeListener != null) {
                    cartChangeListener.onCartChanged();
                }
            }
        });

        // Handle plus button
        holder.btnCartPlus.setOnClickListener(v -> {
            int quantity = meal.getQuantity();
            quantity++;
            meal.setQuantity(quantity);
            holder.tvCartQuantity.setText(String.valueOf(quantity));

            // Notify listener about cart change to update total
            if (cartChangeListener != null) {
                cartChangeListener.onCartChanged();
            }
        });

        // Handle remove button
        holder.btnRemoveFromCart.setOnClickListener(v -> {
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());

            // Notify listener about cart change to update total
            if (cartChangeListener != null) {
                cartChangeListener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // ViewHolder class
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvCartMealName, tvCartMealPrice, tvCartQuantity;
        ImageView imgCartMeal;
        Button btnCartMinus, btnCartPlus, btnRemoveFromCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link UI elements
            tvCartMealName = itemView.findViewById(R.id.tvCartMealName);
            tvCartMealPrice = itemView.findViewById(R.id.tvCartMealPrice);
            imgCartMeal = itemView.findViewById(R.id.imgCartMeal);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuantity);
            btnCartMinus = itemView.findViewById(R.id.btnCartMinus);
            btnCartPlus = itemView.findViewById(R.id.btnCartPlus);
            btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart);
        }
    }

    // Interface for cart change callback
    public interface OnCartChangeListener {
        void onCartChanged();
    }
}