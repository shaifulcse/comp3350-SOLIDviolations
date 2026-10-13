package com.example.expenses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.example.expenses.logic.BudgetChecker;
import com.example.expenses.logic.ExpenseService;
import com.example.expenses.logic.ExpenseSummary;
import com.example.expenses.logic.ValidationException;
import com.example.expenses.objects.Category;
import com.example.expenses.objects.Expense;
import com.example.expenses.util.MoneyFormatter;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ExpenseLogicTest {

    private ExpenseService service;
    private MoneyFormatter formatter;

    @Before
    public void setUp() {
        service = new ExpenseService();
        formatter = new MoneyFormatter();
    }

    @Test
    public void add_trimsTitleAndStores() throws Exception {
        int before = service.getAll().size();
        Expense e = service.add("  Coffee   beans ", 14.99, Category.FOOD);
        assertEquals("Coffee beans", e.getTitle());
        assertEquals(before + 1, service.getAll().size());
    }

    @Test
    public void add_rejectsEmptyTitle() {
        try {
            service.add("   ", 5, Category.FUN);
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertEquals(ValidationException.Field.TITLE, e.getField());
        }
    }

    @Test
    public void add_rejectsZeroAmount() {
        try {
            service.add("Snack", 0, Category.FOOD);
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertEquals(ValidationException.Field.AMOUNT, e.getField());
        }
    }

    @Test
    public void delete_removesNewExpense() throws Exception {
        Expense e = service.add("Taxi", 20, Category.TRANSPORT);
        assertTrue(service.delete(e.getId()));
        assertFalse(service.delete(e.getId()));
    }

    @Test
    public void formatter_formatsDollars() {
        assertEquals("$0.05", formatter.format(0.05));
        assertEquals("$12.50", formatter.format(12.5));
        assertEquals("$1,234.50", formatter.format(1234.5));
        assertEquals("-$1,000,000.00", formatter.format(-1_000_000));
    }

    @Test
    public void summary_totalsByCategory() {
        List<Expense> list = Arrays.asList(
                new Expense("A", 10, Category.FOOD),
                new Expense("B", 5.5, Category.FOOD),
                new Expense("C", 3, Category.FUN));
        ExpenseSummary summary = new ExpenseSummary(formatter);
        assertEquals(18.5, summary.total(list), 0.0001);
        assertEquals(15.5, summary.totalFor(list, Category.FOOD), 0.0001);
    }

    @Test
    public void budget_flagsOverspending() {
        BudgetChecker checker = new BudgetChecker();
        assertTrue(checker.isOverBudget(Category.FUN, 150));
        assertFalse(checker.isOverBudget(Category.BILLS, 55));
    }
}
