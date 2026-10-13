package com.example.expenses.persistence;

import com.example.expenses.objects.Expense;

import java.util.List;

public interface ExpenseRepository {

    /** @return all expenses, newest first. */
    List<Expense> getAll();

    /** @return the stored expense, with its new id. */
    Expense add(Expense expense);

    /** @return true if the expense was deleted, false if no expense has that id. */
    boolean delete(long id);

    /** @return every expense as CSV text. */
    String exportToCsv();

    /** @return true if the upload succeeded. */
    boolean syncWithServer(String serverUrl);

    void scheduleAutoBackup(int everyHours);
}
