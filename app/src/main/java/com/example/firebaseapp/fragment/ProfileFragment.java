package com.example.firebaseapp.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.firebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private EditText edtUserName,edtEmailAddress;
    private Button btnUpdateProfile;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        edtUserName = view.findViewById(R.id.userName);
        edtEmailAddress = view.findViewById(R.id.emailAddress);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        progressBar = view.findViewById(R.id.progressBarProfile);

        loadUserData();

        btnUpdateProfile.setOnClickListener(v -> {
            String newName = edtUserName.getText().toString().trim();
            String newEmail = edtEmailAddress.getText().toString().trim();

            if (TextUtils.isEmpty(newName)) {
                edtUserName.setError("Please enter your name");
                return;
            }
            if (TextUtils.isEmpty(newEmail)) {
                edtEmailAddress.setError("Please enter your email");
                return;
            }

            updateUserData(newName, newEmail);
        });

        return view;
    }

    private void loadUserData() {
        progressBar.setVisibility(View.VISIBLE);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String name = document.getString("Name");
                        String email = document.getString("Email");

                        // Set the user data in EditText fields
                        edtUserName.setText(name);
                        edtEmailAddress.setText(email);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    private void updateUserData(String newName, String newEmail) {
        progressBar.setVisibility(View.VISIBLE);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Map<String, Object> userData = new HashMap<>();
            userData.put("Name", newName);
            userData.put("Email", newEmail);

            db.collection("users").document(userId)
                    .update(userData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    });
        }
    }
}