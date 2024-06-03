package com.example.firebaseapp.fragment;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.Adapter.CartAdapter;
import com.example.firebaseapp.Model.CartItem;
import com.example.firebaseapp.Model.Order;
import com.example.firebaseapp.Model.OrderItem;
import com.example.firebaseapp.Model.Product;
import com.example.firebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList = new ArrayList<>();
    private Button btnPlaceOrder;
    private TextView totalAmountTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(cartItemList, this::onQuantityChanged);
        recyclerView.setAdapter(cartAdapter);
        totalAmountTextView = view.findViewById(R.id.totalTextView);
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchCartItems();
        btnPlaceOrder.setOnClickListener(v -> placeOrder());

        return view;
    }

    private void fetchCartItems() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("carts").document(userId).collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cartItemList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        CartItem cartItem = document.toObject(CartItem.class);
                        cartItemList.add(cartItem);
                    }
                    cartAdapter.notifyDataSetChanged();
                    calculateTotalAmount();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error fetching cart items", Toast.LENGTH_SHORT).show();
                });
    }

    private void calculateTotalAmount() {
        double totalAmount = 0;
        for (CartItem cartItem : cartItemList) {
            totalAmount += cartItem.getPrice() * cartItem.getQuantity();
        }
        totalAmountTextView.setText(String.format("Total: ₹%.2f", totalAmount));
    }

    public void onQuantityChanged(CartItem cartItem) {
        calculateTotalAmount();
    }

    private void placeOrder() {
        String userId = auth.getCurrentUser().getUid();
        List<Product> orderProducts = new ArrayList<>();
        double totalAmount = 0;

        for (CartItem cartItem : cartItemList) {
            Product product = new Product(
                    cartItem.getProductId(),
                    cartItem.getTitle(),
                    cartItem.getImageUrl(),
                    cartItem.getPrice(),
                    cartItem.getDescription(),
                    cartItem.getRating(),
                    cartItem.getQuantity()
            );
            orderProducts.add(product);
            totalAmount += cartItem.getPrice() * cartItem.getQuantity();
        }

        Order order = new Order(userId, orderProducts, totalAmount, "pending", System.currentTimeMillis());

        db.collection("orders").add(order)
                .addOnSuccessListener(documentReference -> clearCart(userId))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error placing order", Toast.LENGTH_SHORT).show());
    }
    private void clearCart(String userId) {
        db.collection("carts").document(userId).collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                    cartItemList.clear();
                    cartAdapter.notifyDataSetChanged();
                    calculateTotalAmount();
                    Toast.makeText(getContext(), "Order placed successfully and cart cleared", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error clearing cart", Toast.LENGTH_SHORT).show();
                });
    }
}