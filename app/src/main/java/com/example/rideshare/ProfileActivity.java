package com.example.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEt, emailEt, phoneEt;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // XML ka naam yahi hona chahiye

        // IDs XML ke according
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        phoneEt = findViewById(R.id.phoneEt);
        saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(v -> {
            String phone = phoneEt.getText().toString().trim();

            if (!phone.isEmpty()) {
                // OtpActivity open karega
                Intent intent = new Intent(ProfileActivity.this, OtpActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            } else {
                phoneEt.setError("Enter phone number");
            }
        });
    }
}
