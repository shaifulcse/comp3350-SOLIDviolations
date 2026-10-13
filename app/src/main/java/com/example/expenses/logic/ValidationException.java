package com.example.expenses.logic;

/** Thrown when user input breaks a business rule. */
public class ValidationException extends Exception {

    public enum Field { TITLE, AMOUNT }

    private final Field field;

    public ValidationException(Field field, String message) {
        super(message);
        this.field = field;
    }

    public Field getField() {
        return field;
    }
}
