package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BudgetTracker.db";
    private static final int DATABASE_VERSION = 2;

    public static final String USERS_TABLE = "users";
    public static final String TRANSACTIONS_TABLE = "transactions";

    public static final String USER_COL_ID = "ID";
    public static final String USER_COL_USERNAME = "USERNAME";
    public static final String USER_COL_PASSWORD = "PASSWORD";

    public static final String TRANS_COL_ID = "ID";
    public static final String TRANS_COL_AMOUNT = "amount";
    public static final String TRANS_COL_CATEGORY = "category";
    public static final String TRANS_COL_TYPE = "transaction_type";
    public static final String TRANS_COL_IMAGE_URI = "image_uri";
    public static final String TRANS_COL_USERNAME = "username";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + USERS_TABLE + " (" +
                USER_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COL_USERNAME + " TEXT UNIQUE, " +
                USER_COL_PASSWORD + " TEXT)";

        String createTransactionsTable = "CREATE TABLE " + TRANSACTIONS_TABLE + " (" +
                TRANS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TRANS_COL_AMOUNT + " INTEGER, " +
                TRANS_COL_CATEGORY + " TEXT, " +
                TRANS_COL_TYPE + " TEXT, " +
                TRANS_COL_IMAGE_URI + " TEXT, " +
                TRANS_COL_USERNAME + " TEXT, " +
                "FOREIGN KEY(" + TRANS_COL_USERNAME + ") REFERENCES " + USERS_TABLE + "(" + USER_COL_USERNAME + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createTransactionsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(db);
    }

    // Method to insert a new user into the database
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_USERNAME, username);
        contentValues.put(USER_COL_PASSWORD, password);

        long result = db.insert(USERS_TABLE, null, contentValues);
        db.close();
        return result != -1; // If result is -1, it means insertion failed
    }

    // Method to check if the username already exists in the database
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USER_COL_USERNAME + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Method to check user credentials (for login)
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USER_COL_USERNAME + "=? AND " + USER_COL_PASSWORD + "=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Method to insert a transaction
    public boolean insertTransaction(int amount, String category, String transactionType, String imageUri, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANS_COL_AMOUNT, amount);
        contentValues.put(TRANS_COL_CATEGORY, category);
        contentValues.put(TRANS_COL_TYPE, transactionType);
        contentValues.put(TRANS_COL_IMAGE_URI, imageUri);
        contentValues.put(TRANS_COL_USERNAME, username);

        long result = db.insert(TRANSACTIONS_TABLE, null, contentValues);
        Log.d("DBHelper", result == -1 ? "Failed to insert transaction for user: " + username : "Transaction inserted successfully for user: " + username);
        db.close();
        return result != -1;
    }

    @SuppressLint("Range")
    // Method to retrieve all transactions of a user
    public List<Transaction> getUserTransactions(String username) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TRANSACTIONS_TABLE, null, TRANS_COL_USERNAME + " = ?", new String[]{username}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int amount = cursor.getInt(cursor.getColumnIndex(TRANS_COL_AMOUNT));
                    String category = cursor.getString(cursor.getColumnIndex(TRANS_COL_CATEGORY));
                    String transactionType = cursor.getString(cursor.getColumnIndex(TRANS_COL_TYPE));
                    String imageUri = cursor.getString(cursor.getColumnIndex(TRANS_COL_IMAGE_URI));
                    transactions.add(new Transaction(amount, category, transactionType, imageUri));
                    Log.d("DBHelper", "Transaction - Amount: " + amount + ", Category: " + category + ", Type: " + transactionType + ", Image URI: " + imageUri);
                } while (cursor.moveToNext());
            } else {
                Log.d("DBHelper", "No transactions found for user: " + username);
            }
            cursor.close();
        }

        db.close();
        return transactions;
    }

    // Method to retrieve all transactions (for history, etc.)
    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TRANSACTIONS_TABLE, null);
        Log.d("DBHelper", cursor != null && cursor.getCount() == 0 ? "No transactions found in the database." : "Transactions retrieved successfully.");
        return cursor;
    }

    // Transaction class to represent the transaction data
    public class Transaction {
        private int amount;
        private String category;
        private String transactionType;
        private String imageUri;

        public Transaction(int amount, String category, String transactionType, String imageUri) {
            this.amount = amount;
            this.category = category;
            this.transactionType = transactionType;
            this.imageUri = imageUri;
        }

        // Getters for each field
        public int getAmount() {
            return amount;
        }

        public String getCategory() {
            return category;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public String getImageUri() {
            return imageUri;
        }
    }
}
