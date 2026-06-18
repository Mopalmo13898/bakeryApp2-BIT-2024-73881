package com.example.neema_cakery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private DatabaseHelper dbHelper;
    private TextView tvTotal;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);
        tvTotal = findViewById(R.id.tvTotalAmount);
        btnCheckout = findViewById(R.id.btnCheckout);
        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("TOTAL_AMOUNT", calculateTotal());
                startActivity(intent);
            }
        });
    }

    private void loadCart() {
        cartItems = new ArrayList<>();
        Cursor cursor = dbHelper.getCartItems();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PROD_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PRICE));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_QTY));
                cartItems.add(new CartItem(id, name, price, qty));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new CartAdapter(cartItems, (cartId, position) -> {
            dbHelper.removeFromCart(cartId);
            cartItems.remove(position);
            adapter.notifyItemRemoved(position);
            updateTotal();
        });
        recyclerView.setAdapter(adapter);
        updateTotal();
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice();
        }
        return total;
    }

    private void updateTotal() {
        tvTotal.setText(String.format(Locale.getDefault(), "KSH %.0f", calculateTotal()));
    }
}
