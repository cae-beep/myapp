package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class UserFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private TextView textUsername;
    private ImageButton imgProfilePicture;
    private ImageView imgEditIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        textUsername = view.findViewById(R.id.username);
        imgProfilePicture = view.findViewById(R.id.profile_picture);
        imgEditIcon = view.findViewById(R.id.img_edit_icon); // Initialize the pencil icon

        Button btnNotifications = view.findViewById(R.id.btn_notifications);
        Button btnSettings = view.findViewById(R.id.btn_settings);
        Button btnAboutUs = view.findViewById(R.id.btn_about_us);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        // Fetch the username from shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "User");
        textUsername.setText(username);

        // Set up profile picture selection
        imgProfilePicture.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openImageChooser();
            }
        });

        // Set up pencil icon click to open image chooser
        imgEditIcon.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openImageChooser();
            }
        });

        // Notifications button action
        btnNotifications.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Notifications Clicked", Toast.LENGTH_SHORT).show();
        });

        // Settings button action
        btnSettings.setOnClickListener(v -> showSettingsDialog());

        // About Us button action
        btnAboutUs.setOnClickListener(v -> showAboutUsDialog());

        // Logout button action
        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imgProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(getActivity(), "Permission denied to access gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logout() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void showAboutUsDialog() {
        if (getActivity() == null) return;

        new AlertDialog.Builder(getActivity())
                .setTitle("About Us")
                .setMessage("Developed by:\n" +
                        " - Cadangan, Divine Greycel Mae\n" +
                        " - Cosico, Nemaree\n" +
                        " - Malabanan, Louella Mariz\n" +
                        " - Tacla, Eloisa Caryl\n" +
                        " - Villegas, Frances Shereen\n\n" +
                        "University of Batangas Lipa Campus\n" +
                        "College of Information Technology\n\n" +
                        "Contact: 09123456789")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showSettingsDialog() {
        try {
            if (!isAdded() || getActivity() == null) {
                Log.e("UserFragment", "Fragment not added or activity is null");
                return;
            }

            final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_settings, null);
            final Switch switchTheme = dialogView.findViewById(R.id.switch_theme);

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                switchTheme.setChecked(true);
            } else {
                switchTheme.setChecked(false);
            }

            new AlertDialog.Builder(getActivity())
                    .setTitle("Settings")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
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
