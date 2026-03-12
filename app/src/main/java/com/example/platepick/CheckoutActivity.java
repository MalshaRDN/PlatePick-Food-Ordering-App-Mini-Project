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

        // Place Order Button Click Listener
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values and remove extra spaces using .trim()
                String address = etAddress.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                // Regex pattern for a 10-digit phone number starting with '0'
                String phonePattern = "^0[0-9]{9}$";

                // Validation 1: Check if fields are empty
                if (address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Please fill in all delivery details", Toast.LENGTH_SHORT).show();
                    if (address.isEmpty()) etAddress.setError("Delivery address is required");
                    if (phone.isEmpty()) etPhone.setError("Phone number is required");
                }
                // Validation 2: Check if address is too short (less than 10 characters)
                else if (address.length() < 10) {
                    etAddress.setError("Please enter a complete delivery address");
                    etAddress.requestFocus();
                }
                // Validation 3: Check if phone number is valid
                else if (!phone.matches(phonePattern)) {
                    etPhone.setError("Please enter a valid 10-digit phone number (e.g. 0712345678)");
                    etPhone.requestFocus();
                }
                // If all validations pass, proceed with placing the order
                else {
                    // Order is successful
                    Toast.makeText(CheckoutActivity.this, "Order Placed Successfully! Food is on the way.", Toast.LENGTH_LONG).show();

                    // Clear the cart since the order is done
                    CartManager.getInstance().getCartItems().clear();

                    // Navigate back to MainActivity (Home Screen)
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                    // Clear previous activities so user can't press back to go to checkout again
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}