package com.example.expenses.logic;

import com.example.expenses.objects.Category;
import com.example.expenses.objects.Expense;
import com.example.expenses.persistence.SampleExpenseRepository;

import java.util.List;

/** Business rules for adding, listing and deleting expenses. */
public class ExpenseService {

    public static final int MAX_TITLE_LENGTH = 50;
    public static final double MAX_AMOUNT = 100_000;

    private final SampleExpenseRepository repository;

    public ExpenseService() {
        this.repository = new SampleExpenseRepository();
    }

    public List<Expense> getAll() {
        return repository.getAll();
    }

    public Expense add(String title, double amount, Category category) throws ValidationException {
        String t = title == null ? "" : title.trim().replaceAll("\\s+", " ");
        if (t.isEmpty()) {
            throw new ValidationException(ValidationException.Field.TITLE, "Title is required");
        }
        if (t.length() > MAX_TITLE_LENGTH) {
            throw new ValidationException(ValidationException.Field.TITLE,
                    "Title must be at most " + MAX_TITLE_LENGTH + " characters");
        }
        if (amount <= 0) {
            throw new ValidationException(ValidationException.Field.AMOUNT,
                    "Amount must be greater than 0");
        }
        if (amount > MAX_AMOUNT) {
            throw new ValidationException(ValidationException.Field.AMOUNT,
                    "Amount is too large");
        }
        return repository.add(new Expense(t, amount, category));
    }

    /** @return false if no expense has that id. */
    public boolean delete(long id) {
        return repository.delete(id);
    }
}
