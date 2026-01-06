package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentExpenses @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {
    operator fun invoke(limit : Int = 4) : Flow<List<Expense>> = expenseRepository.getRecentExpenses(limit)
}