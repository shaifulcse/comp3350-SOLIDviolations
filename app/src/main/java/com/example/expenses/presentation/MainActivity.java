package com.example.expenses.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expenses.ExpensesApp;
import com.example.expenses.R;
import com.example.expenses.logic.BudgetChecker;
import com.example.expenses.logic.ExpenseService;
import com.example.expenses.logic.ExpenseSummary;
import com.example.expenses.objects.Category;
import com.example.expenses.objects.Expense;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Shows the total, any budget warnings and the list of expenses. */
public class MainActivity extends AppCompatActivity {

    private ExpenseService expenseService;
    private ExpenseSummary expenseSummary;
    private BudgetChecker budgetChecker;
    private ExpensesApp app;

    private ExpenseAdapter adapter;
    private TextView tvTotal;
    private TextView tvBudgetWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (ExpensesApp) getApplication();
        expenseService = app.getExpenseService();
        expenseSummary = app.getExpenseSummary();
        budgetChecker = app.getBudgetChecker();

        tvTotal = findViewById(R.id.tv_total);
        tvBudgetWarning = findViewById(R.id.tv_budget_warning);

        ListView listView = findViewById(R.id.lv_expenses);
        listView.setEmptyView(findViewById(R.id.tv_empty));
        adapter = new ExpenseAdapter(this, app.getMoneyFormatter());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Expense expense = adapter.getItem(position);
            if (expense != null) {
                confirmDelete(expense);
            }
            return true;
        });

        findViewById(R.id.btn_add).setOnClickListener(v ->
                startActivity(new Intent(this, AddExpenseActivity.class)));
        findViewById(R.id.btn_share).setOnClickListener(v -> shareSummary());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        List<Expense> expenses = expenseService.getAll();
        adapter.setExpenses(expenses);
        tvTotal.setText(app.getMoneyFormatter().format(expenseSummary.total(expenses)));

        List<String> over = new ArrayList<>();
        for (Category category : Category.values()) {
            if (budgetChecker.isOverBudget(category, expenseSummary.totalFor(expenses, category))) {
                over.add(category.getLabel());
            }
        }
        if (over.isEmpty()) {
            tvBudgetWarning.setVisibility(View.GONE);
        } else {
            tvBudgetWarning.setText(getString(R.string.over_budget, TextUtils.join(", ", over)));
            tvBudgetWarning.setVisibility(View.VISIBLE);
        }
    }

    private void confirmDelete(Expense expense) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.title_delete_expense)
                .setMessage(getString(R.string.confirm_delete, expense.getTitle()))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    boolean deleted = expenseService.delete(expense.getId());
                    Toast.makeText(this,
                            deleted ? R.string.expense_deleted : R.string.expense_not_found,
                            Toast.LENGTH_SHORT).show();
                    refresh();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void shareSummary() {
        List<Expense> expenses = expenseService.getAll();
        try {
            File file = expenseSummary.saveToFile(expenses, getFilesDir());
            Toast.makeText(this, getString(R.string.summary_saved, file.getName()),
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.summary_save_failed, Toast.LENGTH_SHORT).show();
        }

        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, expenseSummary.buildText(expenses));
        startActivity(Intent.createChooser(send, getString(R.string.share_with)));
    }
}
