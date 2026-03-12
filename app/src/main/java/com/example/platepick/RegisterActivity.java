package com.example.platepick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns; // Added for email validation
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // Declare variables for UI elements and DatabaseHelper
    private EditText etRegName, etRegEmail, etRegPassword, etRegConfirmPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Link UI elements from XML using their IDs
        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input from user and remove extra spaces using .trim()
                String name = etRegName.getText().toString().trim();
                String email = etRegEmail.getText().toString().trim();
                String pass = etRegPassword.getText().toString().trim();
                String confirmPass = etRegConfirmPassword.getText().toString().trim();

                // Regex pattern for strong password (min 8 chars, 1 uppercase, 1 lowercase, 1 number)
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

                // Validation 1: Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                // Validation 2: Check email format
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etRegEmail.setError("Please enter a valid email address");
                    etRegEmail.requestFocus(); // Focus cursor on error field
                }
                // Validation 3: Check for strong password using regex
                else if (!pass.matches(passwordPattern)) {
                    etRegPassword.setError("Password must be at least 8 characters long, with 1 uppercase, 1 lowercase, and 1 number");
                    etRegPassword.requestFocus();
                }
                // Validation 4: Check if passwords match
                else if (!isPasswordMatch(pass, confirmPass)) {
                    etRegConfirmPassword.setError("Passwords do not match!");
                    etRegConfirmPassword.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Passwords do not match! Please try again.", Toast.LENGTH_SHORT).show();
                }
                // If all validations pass, save to database
                else {
                    // Try to insert data into SQLite database
                    boolean isInserted = dbHelper.insertData(name, email, pass);

                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Close this screen

                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed! Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Function to validate if passwords match
    private boolean isPasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}