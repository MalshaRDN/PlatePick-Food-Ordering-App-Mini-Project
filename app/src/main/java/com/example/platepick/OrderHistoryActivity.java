package com.example.platepick;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView lvOrderHistory;
    private TextView tvEmptyOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        lvOrderHistory = findViewById(R.id.lvOrderHistory);
        tvEmptyOrderHistory = findViewById(R.id.tvEmptyOrderHistory);

        loadOrderHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        List<String> orderSummaries = OrderHistoryManager.getOrderSummaries(this);

        if (orderSummaries.isEmpty()) {
            tvEmptyOrderHistory.setVisibility(View.VISIBLE);
            lvOrderHistory.setVisibility(View.GONE);
            return;
        }

        tvEmptyOrderHistory.setVisibility(View.GONE);
        lvOrderHistory.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.order_history_item,
                R.id.tvOrderHistoryItem,
                orderSummaries
        );
        lvOrderHistory.setAdapter(adapter);
    }
}