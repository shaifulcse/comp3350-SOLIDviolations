package com.example.expenses.presentation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expenses.ExpensesApp;
import com.example.expenses.R;
import com.example.expenses.logic.ExpenseService;
import com.example.expenses.logic.ValidationException;
import com.example.expenses.objects.Category;
import com.example.expenses.util.AmountParser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/** Form for adding a new expense. */
public class AddExpenseActivity extends AppCompatActivity {

    private ExpenseService expenseService;
    private AmountParser amountParser;

    private TextInputLayout tilTitle, tilAmount;
    private TextInputEditText etTitle, etAmount;
    private Spinner spCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle(R.string.title_add_expense);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ExpensesApp app = (ExpensesApp) getApplication();
        expenseService = app.getExpenseService();
        amountParser = app.getAmountParser();

        tilTitle = findViewById(R.id.til_title);
        tilAmount = findViewById(R.id.til_amount);
        etTitle = findViewById(R.id.et_title);
        etAmount = findViewById(R.id.et_amount);
        spCategory = findViewById(R.id.sp_category);

        ArrayAdapter<Category> categories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Category.values());
        categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categories);

        findViewById(R.id.btn_save).setOnClickListener(v -> save());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());
    }

    private void save() {
        tilTitle.setError(null);
        tilAmount.setError(null);

        double amount;
        try {
            amount = amountParser.parse(textOf(etAmount));
        } catch (NumberFormatException e) {
            tilAmount.setError(getString(R.string.invalid_amount));
            etAmount.requestFocus();
            return;
        }

        try {
            expenseService.add(textOf(etTitle), amount, (Category) spCategory.getSelectedItem());
            Toast.makeText(this, R.string.expense_added, Toast.LENGTH_SHORT).show();
            finish();
        } catch (ValidationException e) {
            TextInputLayout target =
                    e.getField() == ValidationException.Field.AMOUNT ? tilAmount : tilTitle;
            target.setError(e.getMessage());
            target.requestFocus();
        }
    }

    private static String textOf(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
