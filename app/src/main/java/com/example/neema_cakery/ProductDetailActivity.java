package com.example.neema_cakery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPrice;
    private Button btnAddToCart, btnBuyNow;
    private DatabaseHelper dbHelper;
    private double currentPrice;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new DatabaseHelper(this);

        tvName = findViewById(R.id.tvDetailName);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        tvPrice = findViewById(R.id.tvPriceDetail);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        productName = getIntent().getStringExtra("PRODUCT_NAME");
        String desc = getIntent().getStringExtra("PRODUCT_DESC");
        double basePrice = getIntent().getDoubleExtra("PRODUCT_PRICE", 0.0);
        
        // Mocking the 1.5kg price as per design request
        currentPrice = basePrice * 2.7;

        tvName.setText(productName + " Cake");
        tvDesc.setText(desc);
        tvPrice.setText(String.format(Locale.getDefault(), "KSH %.0f", currentPrice)); 

        btnAddToCart.setOnClickListener(v -> {
            dbHelper.addToCart(productName, currentPrice);
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
        });
        
        btnBuyNow.setOnClickListener(v -> {
            dbHelper.addToCart(productName, currentPrice);
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
