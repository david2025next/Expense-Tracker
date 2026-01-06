package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.service.ExpensePeriodTotals
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getExpensePeriodTotals() : Flow<ExpensePeriodTotals>
    fun getRecentExpenses(limit: Int): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
}