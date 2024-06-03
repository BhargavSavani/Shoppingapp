package com.example.firebaseapp.Ui;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseapp.Model.Product;

import com.example.firebaseapp.R;
import com.example.firebaseapp.fragment.CartFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView productImageView = findViewById(R.id.productDetailImageView);
        TextView productTitleTextView = findViewById(R.id.productDetailTitleTextView);
        TextView productPriceTextView = findViewById(R.id.productDetailPriceTextView);
        TextView productDescriptionTextView = findViewById(R.id.productDetailDescriptionTextView);
        RatingBar productDetailRatingBar = findViewById(R.id.productDetailRatingBar);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            Picasso.get().load(product.getImageUrl()).into(productImageView);
            productDetailRatingBar.setRating(product.getRating());
            productTitleTextView.setText(product.getTitle());
            productPriceTextView.setText(String.format("Price: ₹%.2f", product.getPrice()));
            productDescriptionTextView.setText(product.getDescription());

            btnAddToCart.setOnClickListener(v -> addToCart());

        }
    }

    private void addToCart() {
        String userId = auth.getCurrentUser().getUid();
        String productId = product.getId();

        db.collection("carts").document(userId).collection("items")
                .whereEqualTo("productId", productId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Item already exists in cart, update quantity
                        DocumentSnapshot existingCartItem = queryDocumentSnapshots.getDocuments().get(0);
                        int currentQuantity = existingCartItem.getLong("quantity").intValue();
                        existingCartItem.getReference().update("quantity", currentQuantity)
                                .addOnSuccessListener(aVoid -> {
                                    Intent intent = new Intent(ProductDetailActivity.this, MainActivity2.class);
                                    intent.putExtra("openCart", true);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProductDetailActivity.this, "Error updating cart", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Map<String, Object> cartItem = new HashMap<>();
                        cartItem.put("productId", productId);
                        cartItem.put("imageUrl", product.getImageUrl());
                        cartItem.put("title", product.getTitle());
                        cartItem.put("price", product.getPrice());
                        cartItem.put("quantity", 1);

                        db.collection("carts").document(userId).collection("items")
                                .add(cartItem)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProductDetailActivity.this, MainActivity2.class);
                                    intent.putExtra("openCart", true);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProductDetailActivity.this, "Error adding to cart", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductDetailActivity.this, "Error checking cart", Toast.LENGTH_SHORT).show();
                });
    }
}


