package com.example.platepick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    // Declare UI elements
    private TextView tvCheckoutTotal;
    private EditText etAddress, etPhone;
    private RadioGroup rgPaymentMethod;
    private Button btnPlaceOrder;
    private String userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Link views with XML IDs
        tvCheckoutTotal = findViewById(R.id.tvCheckoutTotal);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // Get the total price passed from CartActivity
        String totalAmount = getIntent().getStringExtra("TOTAL_AMOUNT");
        if (totalAmount != null) {
            tvCheckoutTotal.setText("Total Amount: " + totalAmount);
        }

        userName = getIntent().getStringExtra("USER_NAME");
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Place Order Button Click Listener
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String address = etAddress.getText().toString();
                String phone = etPhone.getText().toString();

                // Validate if fields are empty
                if (address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Please fill in all delivery details", Toast.LENGTH_SHORT).show();
                } else {
                    // Order is successful
                    Toast.makeText(CheckoutActivity.this, "Order Placed Successfully! Food is on the way.", Toast.LENGTH_LONG).show();

                    // Save this order before clearing cart
                    OrderHistoryManager.saveOrder(
                            CheckoutActivity.this,
                            CartManager.getInstance().getCartItems(),
                            totalAmount
                    );

                    // Clear the cart since the order is done
                    CartManager.getInstance().getCartItems().clear();

                    // Navigate back to MainActivity (Home Screen)
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);

                    intent.putExtra("USER_NAME", userName);
                    intent.putExtra("USER_EMAIL", userEmail);
                    // Clear previous activities so user can't press back to go to checkout again
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}