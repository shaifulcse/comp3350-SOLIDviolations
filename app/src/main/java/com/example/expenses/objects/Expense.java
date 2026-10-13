package com.example.expenses.objects;

/** An amount of money spent on something. Immutable. */
public class Expense {

    private final long id;
    private final String title;
    private final double amount;
    private final Category category;

    public Expense(long id, String title, double amount, Category category) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
    }

    /** Creates an expense that has not been stored yet. */
    public Expense(String title, double amount, Category category) {
        this(0, title, amount, category);
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public Category getCategory() { return category; }

    public Expense withId(long newId) {
        return new Expense(newId, title, amount, category);
    }
}
