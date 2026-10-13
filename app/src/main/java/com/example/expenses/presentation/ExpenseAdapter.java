package com.example.expenses.presentation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.expenses.objects.Expense;
import com.example.expenses.util.MoneyFormatter;

import java.util.ArrayList;
import java.util.List;

/** Two-line rows: title and amount on top, category below. */
public class ExpenseAdapter extends ArrayAdapter<Expense> {

    private final MoneyFormatter formatter;

    public ExpenseAdapter(Context context, MoneyFormatter formatter) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1,
                new ArrayList<Expense>());
        this.formatter = formatter;
    }

    public void setExpenses(List<Expense> expenses) {
        setNotifyOnChange(false);
        clear();
        addAll(expenses);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Expense expense = getItem(position);
        if (expense != null) {
            TextView line1 = view.findViewById(android.R.id.text1);
            TextView line2 = view.findViewById(android.R.id.text2);
            line1.setText(expense.getTitle() + "  \u2014  " + formatter.format(expense.getAmount()));
            line2.setText(expense.getCategory().getLabel());
        }
        return view;
    }
}
