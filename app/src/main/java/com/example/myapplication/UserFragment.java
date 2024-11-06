package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {

    private TextView textUsername, textProfileDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        textUsername = view.findViewById(R.id.text_username);
        textProfileDetails = view.findViewById(R.id.text_profile_details);
        Button btnNotifications = view.findViewById(R.id.btn_notifications);
        Button btnSettings = view.findViewById(R.id.btn_settings);
        Button btnAboutUs = view.findViewById(R.id.btn_about_us);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        // Fetch the username from shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "User");
        textUsername.setText(username);
        textProfileDetails.setText("Profile details go here..."); // Customize as needed

        btnNotifications.setOnClickListener(v -> {
            // Handle notifications click
            // Optional: create another fragment or activity to show notifications
        });

        btnSettings.setOnClickListener(v -> {
            // Handle settings click
            // can create a new fragment or activity for settings
        });

        btnAboutUs.setOnClickListener(v -> {
            // Handle about us click
            // Create a dialog or a new fragment/activity to show developer details
        });

        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        // Clear user session and redirect to login
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Clear all stored preferences
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Close the MainActivity
    }
}
