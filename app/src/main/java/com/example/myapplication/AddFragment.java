package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.myapplication.DatabaseHelper; // Adjust package name if necessary


import java.util.Arrays;
import java.util.List;

public class AddFragment extends Fragment {

    private static final int PICK_IMAGE = 1;

    private EditText editTextAmount;
    private RadioGroup radioGroupCategories;
    private EditText editTextOtherCategory;
    private ImageView imageViewPhoto;
    private Button buttonCashIn, buttonCashOut, buttonSave;
    private String transactionType = "Cash In";
    private SQLiteDatabase database;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editTextAmount = view.findViewById(R.id.editText_amount);
        radioGroupCategories = view.findViewById(R.id.radioGroup_categories);
        editTextOtherCategory = view.findViewById(R.id.editText_other_category);
        imageViewPhoto = view.findViewById(R.id.imageView_photo);
        buttonCashIn = view.findViewById(R.id.button_cash_in);
        buttonCashOut = view.findViewById(R.id.button_cash_out);
        buttonSave = view.findViewById(R.id.button_save);

        // Initialize database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getWritableDatabase();

        // Set up button listeners
        buttonCashIn.setOnClickListener(v -> setupCategoriesForCashIn());
        buttonCashOut.setOnClickListener(v -> setupCategoriesForCashOut());

        buttonSave.setOnClickListener(v -> saveTransaction());

        // Show or hide the "Others" text input based on selection
        radioGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_others) {
                editTextOtherCategory.setVisibility(View.VISIBLE);
            } else {
                editTextOtherCategory.setVisibility(View.GONE);
            }
        });

        // Upload photo
        view.findViewById(R.id.button_upload_photo).setOnClickListener(v -> openGallery());

        return view;
    }

    private void setupCategoriesForCashIn() {
        transactionType = "Cash In";
        List<String> categories = Arrays.asList("Salary", "Investments", "Part-time", "Bonus", "Tips", "Others");
        updateCategoryButtons(categories);
    }

    private void setupCategoriesForCashOut() {
        transactionType = "Cash Out";
        List<String> categories = Arrays.asList("Food", "Transportation", "Shopping", "Bills", "Entertainment", "Others");
        updateCategoryButtons(categories);
    }

    private void updateCategoryButtons(List<String> categories) {
        radioGroupCategories.removeAllViews();
        for (String category : categories) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(category);
            radioGroupCategories.addView(radioButton);
        }
        editTextOtherCategory.setVisibility(View.GONE);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageViewPhoto.setImageURI(selectedImageUri);
            imageViewPhoto.setVisibility(View.VISIBLE);
            imageViewPhoto.setTag(selectedImageUri.toString()); // Save URI as tag for later use
        }
    }
    private void saveTransaction() {
        String amountStr = editTextAmount.getText().toString();
        int selectedCategoryId = radioGroupCategories.getCheckedRadioButtonId();

        // Check if required fields are filled
        if (amountStr.isEmpty() || selectedCategoryId == -1) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = ((RadioButton) getView().findViewById(selectedCategoryId)).getText().toString();
        if (category.equals("Others")) {
            String otherCategory = editTextOtherCategory.getText().toString();
            if (otherCategory.isEmpty()) {
                Toast.makeText(getActivity(), "Please specify the other category", Toast.LENGTH_SHORT).show();
                return;
            }
            category = otherCategory;
        }

        // Convert amount to integer
        int amount = Integer.parseInt(amountStr);

        // Prepare data for database insertion
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("category", category);
        values.put("transaction_type", transactionType);

        // Check if an image URI is available, add only if present
        if (imageViewPhoto.getTag() != null) {
            values.put("image_uri", imageViewPhoto.getTag().toString());
        }

        // Insert into SQLite database
        long result = database.insert("transactions", null, values);

        if (result == -1) {
            Toast.makeText(getActivity(), "Failed to save transaction", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Transaction saved", Toast.LENGTH_SHORT).show();
            // Clear the fields after saving
            editTextAmount.setText("");
            editTextOtherCategory.setText("");
            radioGroupCategories.clearCheck();
            imageViewPhoto.setImageURI(null);
            imageViewPhoto.setTag(null); // Reset the tag as well
        }
    }

}
