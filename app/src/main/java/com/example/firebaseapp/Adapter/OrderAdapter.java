package com.example.firebaseapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.Model.Order;
import com.example.firebaseapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (!order.getProducts().isEmpty()) {
            holder.ordertitleTextView.setText(order.getProducts().get(0).getTitle());
            Picasso.get().load(order.getProducts().get(0).getImageUrl()).into(holder.orderProductImageView);
        }

        holder.totalAmountTextView.setText(String.format("Price: " + "₹%.2f", order.getTotalAmount()));
        holder.statusTextView.setText("Status: " + order.getStatus());
        holder.timestampTextView.setText("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(order.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView orderProductImageView;
        TextView ordertitleTextView, totalAmountTextView, statusTextView, timestampTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderProductImageView = itemView.findViewById(R.id.orderProductImageView);
            ordertitleTextView = itemView.findViewById(R.id.ordertitleTextView);
            totalAmountTextView = itemView.findViewById(R.id.orderTotalAmountTextView);
            statusTextView = itemView.findViewById(R.id.orderStatusTextView);
            timestampTextView = itemView.findViewById(R.id.orderDateTextView);
        }
    }
}
