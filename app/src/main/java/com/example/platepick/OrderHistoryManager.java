package com.example.platepick;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryManager {

    private static final String PREFS_NAME = "order_history_prefs";
    private static final String KEY_ORDERS = "orders";

    private OrderHistoryManager() {
    }

    public static void saveOrder(Context context, List<Meal> items, String totalAmount) {
        if (items == null || items.isEmpty()) {
            return;
        }

        JSONArray orders = getOrdersArray(context);
        JSONObject order = new JSONObject();
        JSONArray orderItems = new JSONArray();

        try {
            for (Meal meal : items) {
                JSONObject item = new JSONObject();
                item.put("name", meal.getName());
                item.put("price", meal.getPrice());
                item.put("quantity", meal.getQuantity());
                orderItems.put(item);
            }

            order.put("orderedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
            order.put("total", totalAmount == null ? "0" : totalAmount);
            order.put("items", orderItems);

            orders.put(order);
            saveOrdersArray(context, orders);
        } catch (JSONException ignored) {
        }
    }

    public static List<String> getOrderSummaries(Context context) {
        List<String> summaries = new ArrayList<>();
        JSONArray orders = getOrdersArray(context);

        for (int i = orders.length() - 1; i >= 0; i--) {
            try {
                JSONObject order = orders.getJSONObject(i);
                String orderedAt = order.optString("orderedAt", "");
                String total = order.optString("total", "0");
                JSONArray items = order.optJSONArray("items");

                StringBuilder summary = new StringBuilder();
                summary.append("Ordered At: ").append(orderedAt).append("\n");

                if (items != null) {
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject item = items.getJSONObject(j);
                        summary.append("• ")
                                .append(item.optString("name", "Item"))
                                .append(" x")
                                .append(item.optInt("quantity", 1))
                                .append(" (Rs. ")
                                .append(item.optString("price", "0"))
                                .append(")\n");
                    }
                }

                summary.append("Total: ").append(total);
                summaries.add(summary.toString());
            } catch (JSONException ignored) {
            }
        }

        return summaries;
    }

    private static JSONArray getOrdersArray(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String raw = prefs.getString(KEY_ORDERS, "[]");

        try {
            return new JSONArray(raw);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    private static void saveOrdersArray(Context context, JSONArray orders) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_ORDERS, orders.toString()).apply();
    }
}