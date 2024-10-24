package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Initialize buttons
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignUp);

        // Initially hide buttons
        btnLogin.setVisibility(View.INVISIBLE);
        btnSignup.setVisibility(View.INVISIBLE);

        // Use Handler to show buttons after 3 seconds (3000 milliseconds)
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Make buttons visible after the delay
                btnLogin.setVisibility(View.VISIBLE);
                btnSignup.setVisibility(View.VISIBLE);
            }
        }, 1);

        // Set click listener for Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to LoginActivity
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: finish this activity to prevent going back
            }
        });

        // Set click listener for Signup button
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignupActivity
                Intent intent = new Intent(SplashScreenActivity.this, SignupActivity.class);
                startActivity(intent);
                finish(); // Optional: finish this activity to prevent going back
            }
        });
    }
}