package com.example.expensetracker.utils

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.service.ExpensePeriodTotals
import com.example.expensetracker.presentation.home.ExpenseUi
import com.example.expensetracker.presentation.home.TotalsExpensePeriod

fun ExpensePeriodTotals.toUi() = TotalsExpensePeriod(
    today = today,
    thisWeek = thisWeek,
    thisMonth = thisMonth
)

fun Expense.toUi()  = ExpenseUi(
    title = title,
    amount = amount,
    category = category.name,
    iconCategory = category.icon,
    date = formatDate(date)
)

fun formatDate(date : Long) : String = TODO()