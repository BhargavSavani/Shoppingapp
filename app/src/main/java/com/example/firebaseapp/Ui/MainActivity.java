package com.example.firebaseapp.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button Login, fbLogin, gLogin;
    TextView tvSignup;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        progressbar = findViewById(R.id.progressbar);
        Login = findViewById(R.id.Login);
        fbLogin = findViewById(R.id.facebook_login_button);
        gLogin = findViewById(R.id.google_login_button);
        tvSignup = findViewById(R.id.tvSign);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                progressbar.setVisibility(View.VISIBLE);
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Please Enter Your Email");
                    edtEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Please Enter Your Password");
                    edtPassword.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                            progressbar.setVisibility(View.GONE);

                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Email Address is not Registered.... please signup first!!!", Toast.LENGTH_LONG).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
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
}