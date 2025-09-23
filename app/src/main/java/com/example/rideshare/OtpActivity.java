package com.example.rideshare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private EditText otpEt;
    private Button verifyBtn;
    private String verificationId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpEt = findViewById(R.id.otpEt);
        verifyBtn = findViewById(R.id.verifyBtn);
        mAuth = FirebaseAuth.getInstance();

        String phone = getIntent().getStringExtra("phone");
        if (phone != null) {
            sendOtp(phone);
        }

        verifyBtn.setOnClickListener(v -> {
            String code = otpEt.getText().toString().trim();
            if (verificationId != null && !code.isEmpty()) {
                PhoneAuthCredential credential =
                        PhoneAuthProvider.getCredential(verificationId, code);
                signInWithCredential(credential);
            } else {
                Toast.makeText(OtpActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtp(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                signInWithCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                verificationId = s;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(OtpActivity.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OtpActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
