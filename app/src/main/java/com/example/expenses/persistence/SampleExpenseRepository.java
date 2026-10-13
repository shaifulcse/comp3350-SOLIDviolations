package com.example.expenses.persistence;

import com.example.expenses.objects.Category;
import com.example.expenses.objects.Expense;

import java.util.HashSet;
import java.util.Set;

/** In-memory repository that starts with a few example expenses. */
public class SampleExpenseRepository extends InMemoryExpenseRepository {

    private final Set<Long> sampleIds = new HashSet<>();

    public SampleExpenseRepository() {
        addSample(new Expense("Groceries", 82.40, Category.FOOD));
        addSample(new Expense("Bus pass", 105.00, Category.TRANSPORT));
        addSample(new Expense("Phone bill", 55.00, Category.BILLS));
    }

    private void addSample(Expense expense) {
        sampleIds.add(add(expense).getId());
    }

    @Override
    public synchronized boolean delete(long id) {
        if (sampleIds.contains(id)) {
            throw new UnsupportedOperationException("Sample expenses cannot be deleted");
        }
        return super.delete(id);
    }
}
