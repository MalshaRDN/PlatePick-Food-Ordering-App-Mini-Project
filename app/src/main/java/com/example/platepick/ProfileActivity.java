package com.example.platepick;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText etProfileUsername, etProfileEmail, etProfilePassword;
    private DatabaseHelper dbHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        etProfileUsername = findViewById(R.id.etProfileUsername);
        etProfileEmail = findViewById(R.id.etProfileEmail);
        etProfilePassword = findViewById(R.id.etProfilePassword);
        Button btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        currentEmail = getIntent().getStringExtra("USER_EMAIL");

        if (currentEmail == null || currentEmail.trim().isEmpty()) {
            Toast.makeText(this, "Unable to load profile.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProfile(currentEmail);

        btnUpdateProfile.setOnClickListener(v -> updateProfile());
    }

    private void loadProfile(String email) {
        String[] profileData = dbHelper.getUserProfile(email);
        if (profileData != null) {
            etProfileUsername.setText(profileData[0]);
            etProfileEmail.setText(profileData[1]);
            etProfilePassword.setText(profileData[2]);
        } else {
            Toast.makeText(this, "Profile not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateProfile() {
        String updatedUsername = etProfileUsername.getText().toString().trim();
        String updatedEmail = etProfileEmail.getText().toString().trim();
        String updatedPassword = etProfilePassword.getText().toString().trim();

        if (TextUtils.isEmpty(updatedUsername) || TextUtils.isEmpty(updatedEmail) || TextUtils.isEmpty(updatedPassword)) {
            Toast.makeText(this, "Please fill all profile fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(updatedPassword)) {
            Toast.makeText(this,
                    "Password must be at least 8 characters and include upper/lowercase letters, a number, and a special character.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        boolean updated = dbHelper.updateUserProfile(currentEmail, updatedUsername, updatedEmail, updatedPassword);
        if (updated) {
            currentEmail = updatedEmail;
            Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;

        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(character)) {
                hasLowercase = true;
            } else if (Character.isDigit(character)) {
                hasDigit = true;
            } else {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialCharacter;
    }

}

