package com.example.platepick;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declare views
    private TextView tvWelcomeUser;
    private EditText etSearch;
    private RecyclerView rvBurgers, rvPizzas, rvDrinks, rvMixRice;
    private MealAdapter burgerAdapter, pizzaAdapter, drinkAdapter, mixRiceAdapter;
    private List<Meal> burgerList, pizzaList, drinkList, mixRiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views using IDs from XML
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        etSearch = findViewById(R.id.etSearch);
        rvBurgers = findViewById(R.id.rvBurgers);
        rvPizzas = findViewById(R.id.rvPizzas);
        rvDrinks = findViewById(R.id.rvDrinks);
        rvMixRice = findViewById(R.id.rvMixRice);

        // Get the username passed from LoginActivity
        String userName = getIntent().getStringExtra("USER_NAME");
        if(userName != null) {
            tvWelcomeUser.setText("Hello, " + userName + "!");
        }

        // Set LayoutManagers to HORIZONTAL for all RecyclerViews
        rvBurgers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPizzas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDrinks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMixRice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // ---------------------------------------------------------
        // Create separate lists for each category
        // ---------------------------------------------------------

        // --- BURGERS ---
        burgerList = new ArrayList<>();
        burgerList.add(new Meal("Chicken Burger", "850", R.drawable.chicken_burger));
        burgerList.add(new Meal("Beef Burger", "1050", R.drawable.beef_burger));
        burgerList.add(new Meal("Veggie Burger", "750", R.drawable.veggie_burger));
        burgerList.add(new Meal("Burger 1", "900", R.drawable.burger1));
        burgerList.add(new Meal("Burger 2", "950", R.drawable.burger2));
        burgerList.add(new Meal("Burger 3", "1000", R.drawable.burger3));
        burgerList.add(new Meal("Burger 4", "1100", R.drawable.burger4));
        // Set adapter for burgers with quantity change listener
        burgerAdapter = new MealAdapter(burgerList, (meal, quantity) -> {
            CartManager.getInstance().addToCart(meal, quantity);
        });
        rvBurgers.setAdapter(burgerAdapter);

        // --- PIZZAS ---
        pizzaList = new ArrayList<>();
        pizzaList.add(new Meal("Cheese Pizza", "1500", R.drawable.cheese_pizza));
        pizzaList.add(new Meal("Sausage Pizza", "1800", R.drawable.sausage_pizza));
        pizzaList.add(new Meal("BBQ Chicken", "2200", R.drawable.bbq_chicken_pizza));
        pizzaList.add(new Meal("Pizza 1", "1600", R.drawable.pizza1));
        pizzaList.add(new Meal("Pizza 2", "1700", R.drawable.pizza2));
        pizzaList.add(new Meal("Pizza 3", "1900", R.drawable.pizza3));
        pizzaList.add(new Meal("Pizza 4", "2000", R.drawable.pizza4));
        pizzaList.add(new Meal("Pizza 5", "2100", R.drawable.pizza5));
        // Set adapter for pizzas with quantity change listener
        pizzaAdapter = new MealAdapter(pizzaList, (meal, quantity) -> {
            CartManager.getInstance().addToCart(meal, quantity);
        });
        rvPizzas.setAdapter(pizzaAdapter);

        // --- DRINKS ---
        drinkList = new ArrayList<>();
        drinkList.add(new Meal("Coca Cola", "300", R.drawable.coca_cola));
        drinkList.add(new Meal("Milkshake", "600", R.drawable.milkshake));
        drinkList.add(new Meal("Orange Juice", "450", R.drawable.orange_juice));
        drinkList.add(new Meal("Drink 1", "350", R.drawable.drink1));
        drinkList.add(new Meal("Drink 2", "400", R.drawable.drink2));
        drinkList.add(new Meal("Drink 3", "500", R.drawable.drink3));
        drinkList.add(new Meal("Drink 4", "550", R.drawable.drink4));
        drinkList.add(new Meal("Drink 5", "600", R.drawable.drink5));
        // Set adapter for drinks with quantity change listener
        drinkAdapter = new MealAdapter(drinkList, (meal, quantity) -> {
            CartManager.getInstance().addToCart(meal, quantity);
        });
        rvDrinks.setAdapter(drinkAdapter);

        // --- MIX RICE ---
        mixRiceList = new ArrayList<>();
        mixRiceList.add(new Meal("Mix Rice 1", "650", R.drawable.mixrice1));
        mixRiceList.add(new Meal("Mix Rice 2", "700", R.drawable.mixrice2));
        mixRiceList.add(new Meal("Mix Rice 3", "750", R.drawable.mixrice4));
        mixRiceList.add(new Meal("Mix Rice 4", "800", R.drawable.mixrice5));
        // Set adapter for mix rice with quantity change listener
        mixRiceAdapter = new MealAdapter(mixRiceList, (meal, quantity) -> {
            CartManager.getInstance().addToCart(meal, quantity);
        });
        rvMixRice.setAdapter(mixRiceAdapter);

        // ---------------------------------------------------------
        // Search Bar Functionality
        // ---------------------------------------------------------
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();
                filterMeals(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // ---------------------------------------------------------
        // View Cart button logic (from toolbar)
        // ---------------------------------------------------------
        TextView tvCart = findViewById(R.id.tvCart);
        tvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CartActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    } // <-- Only ONE closing brace for onCreate method

    // Method to filter meals based on search query
    private void filterMeals(String query) {
        if (query.isEmpty()) {
            // Show all meals if search is empty
            burgerAdapter.notifyDataSetChanged();
            pizzaAdapter.notifyDataSetChanged();
            drinkAdapter.notifyDataSetChanged();
            mixRiceAdapter.notifyDataSetChanged();
            rvBurgers.setVisibility(View.VISIBLE);
            rvPizzas.setVisibility(View.VISIBLE);
            rvDrinks.setVisibility(View.VISIBLE);
            rvMixRice.setVisibility(View.VISIBLE);
        } else {
            // Filter meals by name
            List<Meal> filteredBurgers = new ArrayList<>();
            for (Meal meal : burgerList) {
                if (meal.getName().toLowerCase().contains(query)) {
                    filteredBurgers.add(meal);
                }
            }

            List<Meal> filteredPizzas = new ArrayList<>();
            for (Meal meal : pizzaList) {
                if (meal.getName().toLowerCase().contains(query)) {
                    filteredPizzas.add(meal);
                }
            }

            List<Meal> filteredDrinks = new ArrayList<>();
            for (Meal meal : drinkList) {
                if (meal.getName().toLowerCase().contains(query)) {
                    filteredDrinks.add(meal);
                }
            }

            List<Meal> filteredMixRice = new ArrayList<>();
            for (Meal meal : mixRiceList) {
                if (meal.getName().toLowerCase().contains(query)) {
                    filteredMixRice.add(meal);
                }
            }

            // Show/hide categories based on filtered results
            rvBurgers.setVisibility(filteredBurgers.isEmpty() ? View.GONE : View.VISIBLE);
            rvPizzas.setVisibility(filteredPizzas.isEmpty() ? View.GONE : View.VISIBLE);
            rvDrinks.setVisibility(filteredDrinks.isEmpty() ? View.GONE : View.VISIBLE);
            rvMixRice.setVisibility(filteredMixRice.isEmpty() ? View.GONE : View.VISIBLE);

            // Update adapters with filtered lists
            burgerAdapter = new MealAdapter(filteredBurgers, (meal, quantity) -> {
                CartManager.getInstance().addToCart(meal, quantity);
            });
            rvBurgers.setAdapter(burgerAdapter);

            pizzaAdapter = new MealAdapter(filteredPizzas, (meal, quantity) -> {
                CartManager.getInstance().addToCart(meal, quantity);
            });
            rvPizzas.setAdapter(pizzaAdapter);

            drinkAdapter = new MealAdapter(filteredDrinks, (meal, quantity) -> {
                CartManager.getInstance().addToCart(meal, quantity);
            });
            rvDrinks.setAdapter(drinkAdapter);

            mixRiceAdapter = new MealAdapter(filteredMixRice, (meal, quantity) -> {
                CartManager.getInstance().addToCart(meal, quantity);
            });
            rvMixRice.setAdapter(mixRiceAdapter);
        }
    }
}