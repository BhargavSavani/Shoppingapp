package com.example.firebaseapp.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.firebaseapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otpvarification extends AppCompatActivity {
    private EditText edtPhone;
    private Button btngenerate;
    private FirebaseAuth mAuth;
    private String verificationId;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvarification);
        edtPhone = findViewById(R.id.phoneInput);
        pb = findViewById(R.id.pg1);
        btngenerate = findViewById(R.id.BtnGetOtp);

        mAuth = FirebaseAuth.getInstance();

        btngenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                pb.setVisibility(View.VISIBLE);
                String phoneNumber = edtPhone.getText().toString().trim();
                if (!phoneNumber.isEmpty() && phoneNumber.length() == 10) {
                    sendVerificationCode(phoneNumber);
                } else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(Otpvarification.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                }
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

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber, 30, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pb.setVisibility(View.GONE);
                Toast.makeText(Otpvarification.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                showBottomSheet(phoneNumber);
            }
        });
    }
    private void showBottomSheet(String phoneNumber) {

        View bottomSheetView = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Otpvarification.this);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setPeekHeight(700);

        TextView tvphone = bottomSheetView.findViewById(R.id.tvphone);
        PinView pinView = bottomSheetView.findViewById(R.id.pinView);
        TextView timerTextView = bottomSheetView.findViewById(R.id.timerTextView);
        TextView resendtv = bottomSheetView.findViewById(R.id.resendTextview);
        Button changeNumberButton = bottomSheetView.findViewById(R.id.changeNumberButton);
        Button verifyOTPButton = bottomSheetView.findViewById(R.id.verifyOTPButton);

        tvphone.setText("We have sent 6-digit passcode on your mobile number" + "+91" + phoneNumber);


        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time Left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                bottomSheetDialog.dismiss();
                Toast.makeText(Otpvarification.this, "Time's up! Please request a new OTP.", Toast.LENGTH_SHORT).show();
            }
        }.start();

        resendtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(edtPhone.getText().toString().trim());
                Toast.makeText(Otpvarification.this, "OTP Resent", Toast.LENGTH_SHORT).show();
            }
        });

        changeNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Toast.makeText(Otpvarification.this, "Please enter new phone number", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Otpvarification.this, Otpvarification.class));
            }
        });

        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = pinView.getText().toString().trim();
                if (otp.length() == 6) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(Otpvarification.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                pb.setVisibility(View.GONE);
                Toast.makeText(Otpvarification.this, "Verification successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Otpvarification.this, MainActivity2.class);
                startActivity(intent);
            } else {
                pb.setVisibility(View.GONE);
                Toast.makeText(Otpvarification.this, "Verification failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}