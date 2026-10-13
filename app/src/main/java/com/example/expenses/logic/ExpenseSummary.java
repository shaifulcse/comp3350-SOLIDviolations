package com.example.expenses.logic;

import com.example.expenses.objects.Category;
import com.example.expenses.objects.Expense;
import com.example.expenses.util.MoneyFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ExpenseSummary {

    private static final String FILE_NAME = "expense-summary.txt";

    private final MoneyFormatter formatter;

    public ExpenseSummary(MoneyFormatter formatter) {
        this.formatter = formatter;
    }

    public double total(List<Expense> expenses) {
        double sum = 0;
        for (Expense e : expenses) {
            sum += e.getAmount();
        }
        return sum;
    }

    public double totalFor(List<Expense> expenses, Category category) {
        double sum = 0;
        for (Expense e : expenses) {
            if (e.getCategory() == category) {
                sum += e.getAmount();
            }
        }
        return sum;
    }

    public String buildText(List<Expense> expenses) {
        StringBuilder text = new StringBuilder("My expenses\n\n");
        for (Category category : Category.values()) {
            text.append(category.getLabel()).append(": ")
                    .append(formatter.format(totalFor(expenses, category))).append('\n');
        }
        text.append("\nTotal: ").append(formatter.format(total(expenses)));
        return text.toString();
    }

    public File saveToFile(List<Expense> expenses, File directory) throws IOException {
        File file = new File(directory, FILE_NAME);
        try (Writer writer = new FileWriter(file)) {
            writer.write(buildText(expenses));
        }
        return file;
    }
}
