package com.example.firebaseapp.fragment;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.firebaseapp.Adapter.ProductAdapter;
import com.example.firebaseapp.Model.Product;
import com.example.firebaseapp.Ui.GridItemDecoration;
import com.example.firebaseapp.Ui.ProductDetailActivity;
import com.example.firebaseapp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()), container, false);

        binding.rcvMain.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        Resources resources = getResources();

        GridItemDecoration itemDecoration = new GridItemDecoration(resources);
        db = FirebaseFirestore.getInstance();

        binding.rcvMain.addItemDecoration(itemDecoration);
        productAdapter = new ProductAdapter(productList, requireContext(), true);
        binding.rcvMain.setAdapter(productAdapter);


        db.collection("Products").orderBy("title").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());
                    checkIfFavorite(product);
                }

            }
        });
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product selectedProduct = productList.get(position);
                Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
                intent.putExtra("product", selectedProduct);
                startActivity(intent);
            }


            @Override
            public void onFavoriteClick(int position) {
                productAdapter.toggleFavorite(position);
            }
        });

        return binding.getRoot();
    }

    private void checkIfFavorite(Product product) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("favorites").document(userId).collection("items").document(product.getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                            product.setFavorite(true);
                        }
                        productList.add(product);
                        productAdapter.notifyDataSetChanged();
                    });
        } else {
            Log.e(TAG, "checkIfFavorite: No user is currently logged in.");
        }
    }
}
