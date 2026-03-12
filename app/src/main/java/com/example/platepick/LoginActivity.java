package com.example.platepick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns; // Added for email validation
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only declare email and pass once
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();        if (email.isEmpty() || pass.isEmpty()) {
                // Added .trim() to prevent accidental trailing spaces
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();

                // Validation 1: Check if fields are empty
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both Email and Password", Toast.LENGTH_SHORT).show();
                }
                // Validation 2: Check if the email format is correct
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Please enter a valid email address");
                    etEmail.requestFocus();
                }
                // If all validations pass, check the database
                else {
                    boolean isUserExist = dbHelper.checkUser(email, pass);

                    // Inside LoginActivity.java after checking database
                    if (isUserExist) {
                        String name = dbHelper.getUsername(email); // Get name from DB

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USER_NAME", name);    // This key must match MainActivity
                        intent.putExtra("USER_EMAIL", email);
                        startActivity(intent);
                        finish();
                    }else {
                    } else {
                        // Added setError to show a visual warning near the password field
                        etPassword.setError("Invalid Email or Password!");
                        etPassword.requestFocus();
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}