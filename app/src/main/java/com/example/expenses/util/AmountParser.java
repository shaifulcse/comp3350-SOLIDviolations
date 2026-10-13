package com.example.expenses.util;

/** Turns the text typed in the amount field into a dollar amount. */
public class AmountParser {

    /** @throws NumberFormatException if the text is not a number */
    public double parse(String text) {
        String t = text == null ? "" : text.trim();
        if (t.isEmpty()) {
            throw new NumberFormatException("Empty amount");
        }
        if (t.contains(".")) {
            return Double.parseDouble(t);
        }
        return Long.parseLong(t) / 100.0;
    }
}
