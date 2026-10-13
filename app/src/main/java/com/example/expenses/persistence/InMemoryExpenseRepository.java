package com.example.expenses.persistence;

import com.example.expenses.objects.Expense;

import java.util.ArrayList;
import java.util.List;

/** Keeps expenses in memory while the app is running. */
public class InMemoryExpenseRepository implements ExpenseRepository {

    private final List<Expense> expenses = new ArrayList<>();
    private long nextId = 1;

    @Override
    public synchronized List<Expense> getAll() {
        List<Expense> result = new ArrayList<>(expenses);
        result.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        return result;
    }

    @Override
    public synchronized Expense add(Expense expense) {
        Expense stored = expense.withId(nextId++);
        expenses.add(stored);
        return stored;
    }

    @Override
    public synchronized boolean delete(long id) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == id) {
                expenses.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized String exportToCsv() {
        StringBuilder csv = new StringBuilder("id,title,amount,category\n");
        for (Expense e : getAll()) {
            csv.append(e.getId()).append(",\"").append(e.getTitle()).append("\",")
                    .append(e.getAmount()).append(',').append(e.getCategory().name()).append('\n');
        }
        return csv.toString();
    }

    @Override
    public boolean syncWithServer(String serverUrl) {
        return false;
    }

    @Override
    public void scheduleAutoBackup(int everyHours) {
    }
}
