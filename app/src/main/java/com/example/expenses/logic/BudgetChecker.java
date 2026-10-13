package com.example.expenses.logic;

import com.example.expenses.objects.Category;

/** Monthly spending limits per category. */
public class BudgetChecker {

    public double getMonthlyLimit(Category category) {
        switch (category) {
            case FOOD:
                return 300;
            case TRANSPORT:
                return 150;
            case BILLS:
                return 500;
            case FUN:
                return 100;
            default:
                return 0;
        }
    }

    public boolean isOverBudget(Category category, double spent) {
        return spent > getMonthlyLimit(category);
    }
}
