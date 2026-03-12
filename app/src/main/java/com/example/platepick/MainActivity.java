package com.example.platepick;

import android.content.Intent;
import android.content.SharedPreferences; // Added for SharedPreferences
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PROFILE_REQUEST_CODE = 1001;
    private TextView tvWelcomeUser;
    private EditText etSearch;
    private RecyclerView rvBurgers, rvPizzas, rvDrinks, rvMixRice;
    private MealAdapter burgerAdapter, pizzaAdapter, drinkAdapter, mixRiceAdapter;
    private List<Meal> burgerList, pizzaList, drinkList, mixRiceList;
    private String userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. INITIALIZE VIEWS
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        rvBurgers = findViewById(R.id.rvBurgers);
        rvPizzas = findViewById(R.id.rvPizzas);
        rvDrinks = findViewById(R.id.rvDrinks);
        rvMixRice = findViewById(R.id.rvMixRice);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        ImageView ivCart = findViewById(R.id.ivCart);
        ImageView ivOrderHistory = findViewById(R.id.ivOrderHistory);



        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        // If the user is not logged in, redirect them to LoginActivity
        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity
            return; // Stop further execution
        }

        userName = sharedPreferences.getString("USER_NAME", "User");
        userEmail = sharedPreferences.getString("USER_EMAIL", "");


        updateWelcomeText();

        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivityForResult(intent, PROFILE_REQUEST_CODE);
        });

        ivCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });

        ivOrderHistory.setOnClickListener(v -> {
            // FIX: Ensure the email is passed so OrderHistoryActivity knows whose orders to show
            Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });


        rvBurgers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPizzas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDrinks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMixRice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // --- BURGERS ---
        burgerList = new ArrayList<>();
        burgerList.add(new Meal("Chicken Burger", "850", R.drawable.chicken_burger));
        burgerList.add(new Meal("Beef Burger", "1050", R.drawable.beef_burger));
        burgerList.add(new Meal("Veggie Burger", "750", R.drawable.veggie_burger));
        burgerList.add(new Meal("Cheese Burger", "850", R.drawable.burger1));
        burgerList.add(new Meal("Juicy HamBurger", "950", R.drawable.burger2));
        burgerAdapter = new MealAdapter(burgerList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity));
        rvBurgers.setAdapter(burgerAdapter);

        // --- PIZZAS ---
        pizzaList = new ArrayList<>();
        pizzaList.add(new Meal("Margherita Pizza", "1200", R.drawable.pizza1));
        pizzaList.add(new Meal("Pepperoni Pizza", "1500", R.drawable.pizza2));
        pizzaList.add(new Meal("Cheese Pizza", "1500", R.drawable.cheese_pizza));
        pizzaList.add(new Meal("BBQ Chicken Pizza", "1700", R.drawable.bbq_chicken_pizza));
        pizzaList.add(new Meal("Cheese Pizza", "1800", R.drawable.cheese_pizza));

        pizzaAdapter = new MealAdapter(pizzaList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity));
        rvPizzas.setAdapter(pizzaAdapter);

        // --- DRINKS ---
        drinkList = new ArrayList<>();
        drinkList.add(new Meal("Coca Cola", "300", R.drawable.coca_cola));
        drinkList.add(new Meal("Milkshake", "600", R.drawable.milkshake));
        drinkList.add(new Meal("Orange Juice", "600", R.drawable.orange_juice));
        drinkList.add(new Meal("Mocktail", "400", R.drawable.drink1));
        drinkList.add(new Meal("Kivi Juice", "700", R.drawable.drink4));



        drinkAdapter = new MealAdapter(drinkList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity));
        rvDrinks.setAdapter(drinkAdapter);

        // --- MIX RICE ---
        mixRiceList = new ArrayList<>();
        mixRiceList.add(new Meal("Prawn Rice", "1000", R.drawable.mixrice1));
        mixRiceList.add(new Meal("Vegetable Rice", "700", R.drawable.mixrice2));
        mixRiceList.add(new Meal("Chicken Rice", "800", R.drawable.chicken_rice));
        mixRiceList.add(new Meal("Egg Rice", "600", R.drawable.egg_rice));

        mixRiceAdapter = new MealAdapter(mixRiceList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity));
        rvMixRice.setAdapter(mixRiceAdapter);

    }

    private void updateWelcomeText() {
        if (userName != null && !userName.trim().isEmpty()) {
            tvWelcomeUser.setText("Hello, " + userName + "!");
        } else {
            tvWelcomeUser.setText("Hello, User!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String updatedName = data.getStringExtra("UPDATED_USER_NAME");
            String updatedEmail = data.getStringExtra("UPDATED_USER_EMAIL");

            if (updatedName != null && !updatedName.trim().isEmpty()) {
                userName = updatedName;

                // Update SharedPreferences with the new name
                SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                editor.putString("USER_NAME", userName);
                editor.apply();
            }
            if (updatedEmail != null && !updatedEmail.trim().isEmpty()) {
                userEmail = updatedEmail;

                // Update SharedPreferences with the new email
                SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                editor.putString("USER_EMAIL", userEmail);
                editor.apply();
            }

            updateWelcomeText();
        }
    }


    private void filterMeals(String query) {
        if (query.isEmpty()) {
            // Re-setup adapters with original lists
            rvBurgers.setAdapter(new MealAdapter(burgerList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));
            rvPizzas.setAdapter(new MealAdapter(pizzaList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));
            rvDrinks.setAdapter(new MealAdapter(drinkList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));
            rvMixRice.setAdapter(new MealAdapter(mixRiceList, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));

            rvBurgers.setVisibility(View.VISIBLE);
            rvPizzas.setVisibility(View.VISIBLE);
            rvDrinks.setVisibility(View.VISIBLE);
            rvMixRice.setVisibility(View.VISIBLE);
        } else {
            // Filter logic (Simplified for readability)
            List<Meal> fBurgers = filterList(burgerList, query);
            List<Meal> fPizzas = filterList(pizzaList, query);
            List<Meal> fDrinks = filterList(drinkList, query);
            List<Meal> fRice = filterList(mixRiceList, query);

            rvBurgers.setVisibility(fBurgers.isEmpty() ? View.GONE : View.VISIBLE);
            rvPizzas.setVisibility(fPizzas.isEmpty() ? View.GONE : View.VISIBLE);
            rvDrinks.setVisibility(fDrinks.isEmpty() ? View.GONE : View.VISIBLE);
            rvMixRice.setVisibility(fRice.isEmpty() ? View.GONE : View.VISIBLE);

            rvBurgers.setAdapter(new MealAdapter(fBurgers, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));
            rvPizzas.setAdapter(new MealAdapter(fPizzas, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));
            rvDrinks.setAdapter(new MealAdapter(fDrinks, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));
            rvMixRice.setAdapter(new MealAdapter(fRice, (meal, quantity) -> CartManager.getInstance().addToCart(meal, quantity)));
        }
    }

    // Helper method to reduce code repetition
    private List<Meal> filterList(List<Meal> list, String query) {
        List<Meal> filtered = new ArrayList<>();
        for (Meal meal : list) {
            if (meal.getName().toLowerCase().contains(query)) filtered.add(meal);
        }
        return filtered;
    }

}