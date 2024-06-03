package com.example.firebaseapp.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.Model.Product;
import com.example.firebaseapp.R;
import com.example.firebaseapp.Ui.MainActivity2;
import com.example.firebaseapp.Ui.RegistrationActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener onItemClickListener;
    private FirebaseFirestore db;
    private Context context;
    private boolean showFavoriteIcon;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onFavoriteClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ProductAdapter(List<Product> productList, Context context, boolean showFavoriteIcon) {
        this.productList = productList;
        this.context = context;
        this.showFavoriteIcon = showFavoriteIcon;
        db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productTitleTextView.setText(product.getTitle());
        holder.productPriceTextView.setText(String.format("Price: ₹%.2f", product.getPrice()));
        holder.productRatingBar.setRating(product.getRating());
        Picasso.get().load(product.getImageUrl()).into(holder.productImageView);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position);
            }
        });

        if (showFavoriteIcon) {
            holder.favoriteImageView.setVisibility(View.VISIBLE);
            holder.favoriteImageView.setImageResource(product.isFavorite() ? R.drawable.baseline_favorite : R.drawable.baseline_favorite_border);
        } else {
            holder.favoriteImageView.setVisibility(View.GONE);
        }

        holder.favoriteImageView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onFavoriteClick(position);
            }
        });

        holder.favoriteImageView.setOnClickListener(v -> toggleFavorite(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void toggleFavorite(int position) {
        Product product = productList.get(position);
        boolean isFavorite = product.isFavorite();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            showToast("User not logged in");
            return;
        }

        String userId = user.getUid();
        String productId = product.getId();

        if (isFavorite) {
            db.collection("favorites").document(userId).collection("items").document(productId).delete()
                    .addOnSuccessListener(aVoid -> {
                        product.setFavorite(false);
                        notifyItemChanged(position);
                        showToast("Remove from Favorites");
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error removing item from favorites", e));
        } else {
            db.collection("favorites").document(userId).collection("items").document(productId).set(product)
                    .addOnSuccessListener(aVoid -> {
                        product.setFavorite(true);
                        notifyItemChanged(position);
                        showToast("Add to Favorites");
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding item to favorites", e));
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImageView;
        TextView productTitleTextView;
        TextView productPriceTextView;
        RatingBar productRatingBar;
        ImageView favoriteImageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productTitleTextView = itemView.findViewById(R.id.productTitleTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productRatingBar = itemView.findViewById(R.id.productRatingBar);
            favoriteImageView = itemView.findViewById(R.id.favoriteImageView);

        }
    }
}

