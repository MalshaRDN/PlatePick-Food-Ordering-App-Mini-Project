package com.example.platepick;

import android.content.Intent;
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

                    if (isUserExist) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Fetch username from Database to show in MainActivity
                        String username = dbHelper.getUsername(email);

                        // Passing data to MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USER_NAME", username); // Send username to next screen
                        startActivity(intent);
                        finish();
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