package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the username from the Intent
        String username = getIntent().getStringExtra("username");

        // Save username to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username); // Save the username
        editor.apply();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.navigation_history) {
                    selectedFragment = new HistoryFragment();
                } else if (item.getItemId() == R.id.navigation_add) {
                    selectedFragment = new AddFragment();
                } else if (item.getItemId() == R.id.navigation_user) {
                    selectedFragment = new UserFragment();
                } else {
                    return false; // Return false if no valid item is selected
                }

                // Replace fragment in fragment_container
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });
    }
}
