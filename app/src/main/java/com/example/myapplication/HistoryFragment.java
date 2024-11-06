package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_history);
        emptyText = view.findViewById(R.id.empty_text);
        databaseHelper = new DatabaseHelper(getContext());

        loadTransactions(); // Load transactions when the view is created

        return view;
    }

    private void loadTransactions() {
        // Clear existing data in the RecyclerView
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllTransactions();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String transactionType = cursor.getString(cursor.getColumnIndex("transaction_type"));
                transactions.add(new Transaction(amount, category, transactionType));
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Check if the transactions list is empty
        if (transactions.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            TransactionAdapter adapter = new TransactionAdapter(transactions);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        // Load user-specific history
        loadUserHistory();
    }

    private void loadUserHistory() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", null); // Retrieve username

        if (username != null) {

            // code to fetch and display user-specific history
            // code to modify the loadTransactions  method to filter based on the username

        } else {
            // Handle case where username is not found
            emptyText.setText("No user logged in.");
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
