
package com.example.myapplication;

public class Transaction {
    private int amount;
    private String category;
    private String transactionType;
    private String imageUri; // Add this field

    // Constructor
    public Transaction(int amount, String category, String transactionType) {
        this.amount = amount;
        this.category = category;
        this.transactionType = transactionType;
        this.imageUri = imageUri; // Initialize imageUri
    }

    // Getters and setters (if needed)
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
        return imageUri; // Add getter for imageUri
    }
}
