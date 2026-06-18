package com.example.neema_cakery;

public class Customer {
    private int id;
    private String fullName;
    private String phone;
    private String email;
    private String address;

    public Customer(int id, String fullName, String phone, String email, String address) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
}
