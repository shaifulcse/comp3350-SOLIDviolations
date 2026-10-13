package com.example.expenses;

import android.app.Application;

import com.example.expenses.logic.BudgetChecker;
import com.example.expenses.logic.ExpenseService;
import com.example.expenses.logic.ExpenseSummary;
import com.example.expenses.util.AmountParser;
import com.example.expenses.util.MoneyFormatter;

/** Creates the app's shared objects once and hands them to the screens. */
public class ExpensesApp extends Application {

    private ExpenseService expenseService;
    private MoneyFormatter moneyFormatter;
    private AmountParser amountParser;
    private BudgetChecker budgetChecker;
    private ExpenseSummary expenseSummary;

    @Override
    public void onCreate() {
        super.onCreate();
        expenseService = new ExpenseService();
        moneyFormatter = new MoneyFormatter();
        amountParser = new AmountParser();
        budgetChecker = new BudgetChecker();
        expenseSummary = new ExpenseSummary(moneyFormatter);
    }

    public ExpenseService getExpenseService() { return expenseService; }
    public MoneyFormatter getMoneyFormatter() { return moneyFormatter; }
    public AmountParser getAmountParser() { return amountParser; }
    public BudgetChecker getBudgetChecker() { return budgetChecker; }
    public ExpenseSummary getExpenseSummary() { return expenseSummary; }
}
