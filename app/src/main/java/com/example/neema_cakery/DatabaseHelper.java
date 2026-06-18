package com.example.neema_cakery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bakery.db";
    private static final int DATABASE_VERSION = 6; // Incrementing for fresh start

    // Customers Table
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PASSWORD = "password";

    // Products Table
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PROD_ID = "prod_id";
    public static final String COLUMN_PROD_NAME = "prod_name";
    public static final String COLUMN_PROD_PRICE = "prod_price";
    public static final String COLUMN_PROD_DESC = "prod_description";

    // Cart Table
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "cart_id";
    public static final String COLUMN_CART_PROD_NAME = "cart_prod_name";
    public static final String COLUMN_CART_PRICE = "cart_price";
    public static final String COLUMN_CART_QTY = "cart_qty";

    private static final String CREATE_CUSTOMERS =
            "CREATE TABLE " + TABLE_CUSTOMERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FULLNAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT" +
                    ");";

    private static final String CREATE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROD_NAME + " TEXT, " +
                    COLUMN_PROD_PRICE + " REAL, " +
                    COLUMN_PROD_DESC + " TEXT" +
                    ");";

    private static final String CREATE_CART =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CART_PROD_NAME + " TEXT, " +
                    COLUMN_CART_PRICE + " REAL, " +
                    COLUMN_CART_QTY + " INTEGER" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMERS);
        db.execSQL(CREATE_PRODUCTS);
        db.execSQL(CREATE_CART);
        seedProducts(db);
    }

    private void seedProducts(SQLiteDatabase db) {
        // Clear existing to avoid duplicates if re-seeded
        db.delete(TABLE_PRODUCTS, null, null);
        
        insertProduct(db, "Vanilla", 1000.0, "Sweet and creamy classic vanilla bean flavour.");
        insertProduct(db, "Strawberry", 1000.0, "Made with fresh strawberries and a hint of sweetness.");
        insertProduct(db, "Marble", 1000.0, "A perfect blend of chocolate and vanilla swirls.");
        insertProduct(db, "Chocolate", 1000.0, "Rich, moist cocoa layers with velvet finish.");
        insertProduct(db, "Pinacolada", 1000.0, "Tropical pineapple and coconut holiday vibes.");
        insertProduct(db, "Lemon", 1000.0, "Zesty and refreshing citrus burst in every bite.");
        insertProduct(db, "Blueberry", 1000.0, "Bursting with juicy mountain-grown blueberries.");
        insertProduct(db, "Caramel", 1000.0, "Deep, buttery caramel with a touch of sea salt.");
        insertProduct(db, "Funfetti", 1000.0, "Celebration in a cake with colorful sprinkles.");
        insertProduct(db, "Coffee", 1000.0, "Robust espresso flavour for the caffeine lover.");
        insertProduct(db, "Orange", 1000.0, "Tangy and bright citrus flavour from fresh oranges.");
    }

    private void insertProduct(SQLiteDatabase db, String name, double price, String desc) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROD_NAME, name);
        values.put(COLUMN_PROD_PRICE, price);
        values.put(COLUMN_PROD_DESC, desc);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hard reset for development to ensure all tables exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Auth
    public long insertCustomer(String fullname, String phone, String email, String address, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullname);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_CUSTOMERS, null, values);
    }

    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS, new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    // Products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // Cart
    public void addToCart(String name, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_PROD_NAME, name);
        values.put(COLUMN_CART_PRICE, price);
        values.put(COLUMN_CART_QTY, 1);
        db.insert(TABLE_CART, null, values);
    }

    public Cursor getCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CART, null);
    }

    public void removeFromCart(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
    }

    // Customers
    public Cursor getAllCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CUSTOMERS, null);
    }

    public void deleteCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMERS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
