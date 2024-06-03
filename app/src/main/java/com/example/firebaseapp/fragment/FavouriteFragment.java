package com.example.firebaseapp.fragment;

import static android.content.ContentValues.TAG;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.Adapter.ProductAdapter;
import com.example.firebaseapp.Model.Product;
import com.example.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    List<Product> favoriteProducts = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter(favoriteProducts, requireContext(),false);
        recyclerView.setAdapter(adapter);

        fetchDataFromFirestore();
        return view;
    }

    private void fetchDataFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("favorites").document(userId).collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteProducts.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            product.setId(document.getId());
                            favoriteProducts.add(product);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error getting favorites: ", task.getException());
                    }
                });
    }
}
