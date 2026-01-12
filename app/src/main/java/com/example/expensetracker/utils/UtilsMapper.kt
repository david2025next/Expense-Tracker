package com.example.expensetracker.utils

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.findIconByCategoryName
import com.example.expensetracker.domain.service.ExpensePeriodTotals
import com.example.expensetracker.presentation.home.ExpenseItemUiState


fun Expense.toUi()  = ExpenseItemUiState(
    title = title,
    amount = amount,
    category = category,
    iconCategory = findIconByCategoryName(category),
    createdAt = date.toHumanReadableDate()
)
