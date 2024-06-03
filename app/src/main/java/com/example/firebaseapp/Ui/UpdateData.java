package com.example.firebaseapp.Ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseapp.Model.Product;
import com.example.firebaseapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateData extends AppCompatActivity {
    private EditText imageUrl, imageTitle, Price, Description;
    Button Upload;
    FirebaseFirestore db;
    Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        db = FirebaseFirestore.getInstance();

        imageUrl = findViewById(R.id.imageUrl1);
        imageTitle = findViewById(R.id.imageTitle1);
        Price = findViewById(R.id.Price1);
        Description = findViewById(R.id.Description1);
        Upload = findViewById(R.id.Upload1);

        selectedProduct = (Product) getIntent().getSerializableExtra("product");

        if (selectedProduct != null) {
            imageUrl.setText(selectedProduct.getImageUrl());
            imageTitle.setText(selectedProduct.getTitle());
            Price.setText(String.valueOf(selectedProduct.getPrice()));
            Description.setText(selectedProduct.getDescription());
        }

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUrlText = imageUrl.getText().toString().trim();
                String imageTitleText = imageTitle.getText().toString().trim();
                Double priceText = Double.parseDouble(Price.getText().toString().trim());
                String descriptionText = Description.getText().toString().trim();

                Map<String, Object> updates = new HashMap<>();
                updates.put("imageUrl", imageUrlText);
                updates.put("title", imageTitleText);
                updates.put("price", priceText);
                updates.put("description", descriptionText);

                if (selectedProduct != null && !TextUtils.isEmpty(selectedProduct.getId())) {
                    Log.d(TAG, "UpdateData: Product ID: " + selectedProduct.getId());
                    DocumentReference docRef = db.collection("Mobiles").document(selectedProduct.getId());

                    docRef.update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    Toast.makeText(UpdateData.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                    Toast.makeText(UpdateData.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Log.e(TAG, "selectedProduct is null or its ID is empty");
                    Toast.makeText(UpdateData.this, "Failed to update data. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
