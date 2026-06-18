package com.example.neema_cakery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        
        // Setting up vertical layout scrolling top to bottom
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        findViewById(R.id.btnCart).setOnClickListener(v -> {
            startActivity(new Intent(ProductListActivity.this, CartActivity.class));
        });

        loadProducts();
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllProducts();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROD_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROD_PRICE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROD_DESC));

                productList.add(new Product(id, name, price, desc));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ProductAdapter(productList);
        
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_NAME", product.getName());
                intent.putExtra("PRODUCT_DESC", product.getDescription());
                intent.putExtra("PRODUCT_PRICE", product.getPrice());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
