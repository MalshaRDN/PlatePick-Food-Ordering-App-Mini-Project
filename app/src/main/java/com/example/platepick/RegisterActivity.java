package com.example.platepick;

import android.content.Intent;
import android.os.Bundle;
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
                // Get the input from user
                String name = etRegName.getText().toString();
                String email = etRegEmail.getText().toString();
                String pass = etRegPassword.getText().toString();
                String confirmPass = etRegConfirmPassword.getText().toString();

                // Validation: Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordMatch(pass, confirmPass)) {
                    // Check if passwords match
                    Toast.makeText(RegisterActivity.this, "Passwords do not match! Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    // Try to insert data into SQLite database
                    boolean isInserted = dbHelper.insertData(name, email, pass);

                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        // Close this screen and go back to LoginActivity
                        finish();
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