package com.example.firebaseapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.Model.CartItem;
import com.example.firebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    Context context;
    private OnQuantityChangeListener quantityChangeListener;

    public CartAdapter(List<CartItem> cartItems, OnQuantityChangeListener quantityChangeListener) {
        this.cartItems = cartItems;
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.quantityChangeListener = quantityChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.titleTextView.setText(cartItem.getTitle());
        holder.priceTextView.setText(String.format("Price: ₹%.2f", cartItem.getPrice()));
        holder.quantityTextView.setText(String.format("Quantity: %d", cartItem.getQuantity()));
        Picasso.get().load(cartItem.getImageUrl()).into(holder.productImageView);

        holder.ivRemoveFromCart.setOnClickListener(v -> {
            removeItemFromFirestore(cartItem);
            cartItems.remove(position);
            notifyItemRemoved(position);
        });

        holder.ivIncreaseQuantity.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            quantity++;
            cartItem.setQuantity(quantity);
            notifyDataSetChanged();
            quantityChangeListener.onQuantityChanged(cartItem);
        });

        holder.ivDecreaseQuantity.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            if (quantity > 1) {
                quantity--;
                cartItem.setQuantity(quantity);
                notifyDataSetChanged();
                quantityChangeListener.onQuantityChanged(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void removeItemFromFirestore(CartItem cartItem) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("carts").document(userId).collection("items")
                .whereEqualTo("productId", cartItem.getProductId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        document.getReference().delete();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error removing item from cart", Toast.LENGTH_SHORT).show();
                });
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView titleTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageView ivRemoveFromCart;
        ImageView ivIncreaseQuantity;
        ImageView ivDecreaseQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            ivRemoveFromCart = itemView.findViewById(R.id.ivRemoveFromCart);

            ivIncreaseQuantity = itemView.findViewById(R.id.ivIncreaseQuantity);
            ivDecreaseQuantity = itemView.findViewById(R.id.ivDecreaseQuantity);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(CartItem cartItem);
    }
}
