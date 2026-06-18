package com.example.neema_cakery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Customer> customerList;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int customerId, int position);
    }

    public CustomerAdapter(List<Customer> customerList, OnDeleteClickListener listener) {
        this.customerList = customerList;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvName.setText(customer.getFullName());
        holder.tvEmail.setText(customer.getEmail());
        holder.tvPhone.setText(customer.getPhone());

        holder.ivDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(customer.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone;
        ImageView ivDelete;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCustomerName);
            tvEmail = itemView.findViewById(R.id.tvCustomerEmail);
            tvPhone = itemView.findViewById(R.id.tvCustomerPhone);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
