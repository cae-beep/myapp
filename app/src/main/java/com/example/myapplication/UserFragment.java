package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {

    private TextView textUsername, textProfileDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        textUsername = view.findViewById(R.id.text_username);
        Button btnNotifications = view.findViewById(R.id.btn_notifications);
        Button btnSettings = view.findViewById(R.id.btn_settings);
        Button btnAboutUs = view.findViewById(R.id.btn_about_us);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        // Fetch the username from shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "User");
        textUsername.setText(username);

        // Notifications button action
        btnNotifications.setOnClickListener(v -> {
            // Handle notifications click (You can navigate to a new fragment or activity)
            Toast.makeText(getActivity(), "Notifications Clicked", Toast.LENGTH_SHORT).show();
        });

        // Settings button action
        btnSettings.setOnClickListener(v -> {
            // Open a dialog or fragment to toggle dark/light mode
            showSettingsDialog();
        });

        // About Us button action
        btnAboutUs.setOnClickListener(v -> {
            // Show developer info in a dialog
            showAboutUsDialog();
        });

        // Logout button action
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

    // Show the About Us dialog
    private void showAboutUsDialog() {
        if (getActivity() == null) return;

        new AlertDialog.Builder(getActivity())
                .setTitle("About Us")
                .setMessage(" Developed by:\n" +
                        " Cadangan, Divine Greycel Mae\n" +
                        " Cosico, Nemaree\n" +
                        " Malabanan, Louella Mariz\n" +
                        " Tacla, Eloisa Caryl\n" +
                        " Villegas, Frances Shereen\n\n" +
                        " University of Batangas Lipa Campus\n" +
                        " College of Information Technology\n\n" +
                        " Contact \n" +
                        " 09123456789")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showSettingsDialog() {
        try {
            // Ensure the fragment is attached to the activity and is in a valid state
            if (!isAdded() || getActivity() == null) {
                Log.e("UserFragment", "Fragment not added or activity is null");
                return;
            }

            // Inflate the dialog layout
            final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_settings, null);

            // Find the Switch by ID
            final Switch switchTheme = dialogView.findViewById(R.id.switch_theme);

            // Set the switch state based on the current theme
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                switchTheme.setChecked(true);
            } else {
                switchTheme.setChecked(false);
            }

            // Create and show the dialog
            new AlertDialog.Builder(getActivity())
                    .setTitle("Settings")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                        // Apply theme changes based on switch state
                        if (switchTheme.isChecked()) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        Toast.makeText(getActivity(), "Theme updated", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } catch (Exception e) {
            Log.e("UserFragment", "Error showing settings dialog", e);
        }
    }
}
