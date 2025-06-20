package org.example.dailyexpensetrackerproject;

public class Expense {
    private int id;
    private String userEmail;
    private String category;
    private String subcategory;
    private double price;
    private int quantity;
    private String description;
    private String date;

    // 🔹 Constructors
    public Expense() {}

    public Expense(String userEmail, String category, String subcategory, double price, int quantity, String description, String date) {
        this.userEmail = userEmail;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.date = date;
    }

    // 🔹 Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
