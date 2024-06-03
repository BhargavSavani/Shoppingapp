package com.example.firebaseapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firebaseapp.Adapter.OrderAdapter;
import com.example.firebaseapp.Model.Order;
import com.example.firebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrdersFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_orders, container, false);

        recyclerView = view.findViewById(R.id.cordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchCurrentOrders();

        return view;

    }

    private void fetchCurrentOrders() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("orders")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Order order = document.toObject(Order.class);
                        orderList.add(order);
                    }
                    orderAdapter.notifyDataSetChanged();
                    Log.d("CurrentOrdersFragment", "Orders fetched successfully");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error fetching orders", Toast.LENGTH_SHORT).show();
                });
    }
}