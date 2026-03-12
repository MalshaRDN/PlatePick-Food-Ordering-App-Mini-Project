package com.example.platepick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private String userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Link views
        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        userName = getIntent().getStringExtra("USER_NAME");
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Get cart items from CartManager
        List<Meal> cartItems = CartManager.getInstance().getCartItems();

        // Setup RecyclerView
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems, () -> {
            // Callback when cart changes (quantity change or item removed)
            calculateTotal(cartItems);
        });
        rvCartItems.setAdapter(cartAdapter);

        // Calculate and display total price
        calculateTotal(cartItems);

        // Checkout Button Click Listener (Updated)
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the cart is empty
                if (cartItems.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the text from tvTotalPrice (e.g., "Total: Rs. 1500")
                    String fullTotalText = tvTotalPrice.getText().toString();

                    // Extract just the "Rs. 1500" part to pass to the next screen
                    String amountOnly = fullTotalText.replace("Total: ", "");

                    // Navigate to CheckoutActivity
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    intent.putExtra("TOTAL_AMOUNT", amountOnly); // Pass the total amount

                    intent.putExtra("USER_NAME", userName);
                    intent.putExtra("USER_EMAIL", userEmail);
                    startActivity(intent);
                }
            }
        });
    }

    // Method to calculate total amount
    private void calculateTotal(List<Meal> cartItems) {
        double total = 0;
        for (Meal meal : cartItems) {
            // Remove text "Rs." and any spaces, keep only numbers
            String priceString = meal.getPrice().replaceAll("[^0-9]", "");
            try {
                double price = Double.parseDouble(priceString);
                int quantity = meal.getQuantity();
                total += (price * quantity); // Multiply price by quantity
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        // Set total price to TextView
        tvTotalPrice.setText("Total: Rs. " + (int)total);
    }
}