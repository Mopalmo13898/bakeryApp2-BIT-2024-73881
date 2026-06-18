package com.example.neema_cakery;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private List<Customer> customerList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewCustomers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCustomers();
    }

    private void loadCustomers() {
        customerList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllCustomers();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULLNAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS));

                customerList.add(new Customer(id, name, phone, email, address));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new CustomerAdapter(customerList, (customerId, position) -> {
            dbHelper.deleteCustomer(customerId);
            customerList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(this, "Customer deleted", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
    }
}
