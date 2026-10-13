package com.example.expenses.objects;

public enum Category {
    FOOD("Food"),
    TRANSPORT("Transport"),
    BILLS("Bills"),
    FUN("Fun");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
