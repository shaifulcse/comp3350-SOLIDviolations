package com.example.expenses.util;

/** Formats amounts as dollars, e.g. 1234.5 -> "$1,234.50". */
public class MoneyFormatter {

    private static final char CURRENCY_SYMBOL = '$';
    private static final char GROUP_SEPARATOR = ',';
    private static final char DECIMAL_SEPARATOR = '.';
    private static final int GROUP_SIZE = 3;
    private static final int DECIMAL_PLACES = 2;

    public String format(double amount) {
        boolean negative = amount < 0;
        long totalCents = Math.round(Math.abs(amount) * Math.pow(10, DECIMAL_PLACES));

        String digits = Long.toString(totalCents);
        while (digits.length() < DECIMAL_PLACES + 1) {
            digits = "0" + digits;
        }

        String wholePart = digits.substring(0, digits.length() - DECIMAL_PLACES);
        String decimalPart = digits.substring(digits.length() - DECIMAL_PLACES);

        char[] reversed = new StringBuilder(wholePart).reverse().toString().toCharArray();
        StringBuilder groupedReversed = new StringBuilder();
        for (int i = 0; i < reversed.length; i++) {
            if (i > 0 && i % GROUP_SIZE == 0) {
                groupedReversed.append(GROUP_SEPARATOR);
            }
            groupedReversed.append(reversed[i]);
        }
        String groupedWhole = groupedReversed.reverse().toString();

        StringBuilder result = new StringBuilder();
        if (negative && totalCents != 0) {
            result.append('-');
        }
        result.append(CURRENCY_SYMBOL)
                .append(groupedWhole)
                .append(DECIMAL_SEPARATOR)
                .append(decimalPart);
        return result.toString();
    }
}
