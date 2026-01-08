package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.service.ExpensePeriodTotals
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getExpensePeriodTotals() : Flow<ExpensePeriodTotals>
    fun getRecentExpenses(limit: Int = 4): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    fun filter(start : Long, end : Long) : Flow<List<Expense>>
}