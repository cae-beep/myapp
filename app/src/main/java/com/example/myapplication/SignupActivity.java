package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText username, password, confirmPassword;
    Button signup;
    TextView loginText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize the DatabaseHelper and views
        db = new DatabaseHelper(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signup = findViewById(R.id.btnSignUp);
        loginText = findViewById(R.id.btnLogin);  // Find the Login TextView

        // SignUp Button Click Listener
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(confirmPass)) {
                        // Check if the username already exists
                        if (db.isUsernameTaken(user)) {
                            Toast.makeText(SignupActivity.this, "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Insert user into database
                            boolean insert = db.insertUser(user, pass);
                            if (insert) {
                                Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish(); // Close the Signup Activity after successful signup
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Handle the click on "Already have an account? Login" text
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Login Activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the Signup Activity
            }
        });
    }
}
