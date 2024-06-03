package com.example.firebaseapp.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword;
    Button btnSign;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    TextView tvLogin;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(RegistrationActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail1);
        edtPassword = findViewById(R.id.edtPassword1);
        btnSign = findViewById(R.id.Signup);
        progressBar = findViewById(R.id.progressBar);
        tvLogin = findViewById(R.id.Login1);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    edtName.setError("Please Enter Your Name");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Please Enter Your Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Please Enter Your Password");
                    return;
                }
                if (password.length() < 6) {
                    edtPassword.setError("Password must be at least 6 characters");
                    return;
                }

                registerUser(name, email, password);
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void registerUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    String hashedPassword = hashPassword(password);

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("Name", name);
                    userData.put("Email", email);
                    userData.put("Password", hashedPassword);

                    db.collection("users").document(userId)
                            .set(userData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(RegistrationActivity.this, MainActivity2.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(RegistrationActivity.this, "Failed to add user to Firestore", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            });
                }
            } else {
                Toast.makeText(RegistrationActivity.this, "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
