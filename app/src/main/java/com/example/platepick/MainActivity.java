package com.example.platepick;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeUser;
    private RecyclerView rvBurgers, rvPizzas, rvDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Find views
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        rvBurgers = findViewById(R.id.rvBurgers);
        rvPizzas = findViewById(R.id.rvPizzas);
        rvDrinks = findViewById(R.id.rvDrinks);

        // 2. Set Username
        String userName = getIntent().getStringExtra("USER_NAME");
        if(userName != null) {
            tvWelcomeUser.setText("Hello, " + userName + "!");
        }

        // 3. Set LayoutManagers to HORIZONTAL for all RecyclerViews
        rvBurgers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPizzas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDrinks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // 4. Create separate lists for each category

        // --- BURGERS ---
        List<Meal> burgerList = new ArrayList<>();
        burgerList.add(new Meal("Chicken Burger", "Rs. 850", R.mipmap.ic_launcher)); // Change to your image
        burgerList.add(new Meal("Beef Burger", "Rs. 1050", R.mipmap.ic_launcher));
        burgerList.add(new Meal("Veggie Burger", "Rs. 750", R.mipmap.ic_launcher));
        rvBurgers.setAdapter(new MenuAdapter(burgerList));

        // --- PIZZAS ---
        List<Meal> pizzaList = new ArrayList<>();
        pizzaList.add(new Meal("Cheese Pizza", "Rs. 1500", R.mipmap.ic_launcher));
        pizzaList.add(new Meal("Sausage Pizza", "Rs. 1800", R.mipmap.ic_launcher));
        pizzaList.add(new Meal("BBQ Chicken", "Rs. 2200", R.mipmap.ic_launcher));
        rvPizzas.setAdapter(new MenuAdapter(pizzaList));

        // --- DRINKS ---
        List<Meal> drinkList = new ArrayList<>();
        drinkList.add(new Meal("Coca Cola", "Rs. 300", R.mipmap.ic_launcher));
        drinkList.add(new Meal("Milkshake", "Rs. 600", R.mipmap.ic_launcher));
        drinkList.add(new Meal("Orange Juice", "Rs. 450", R.mipmap.ic_launcher));
        rvDrinks.setAdapter(new MenuAdapter(drinkList));
    }
}