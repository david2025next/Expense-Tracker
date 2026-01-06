package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensePeriodTotals(
    private val expenseRepository: ExpenseRepository
) {

    operator fun invoke() : Flow<ExpensePeriodTotals> = expenseRepository.getExpensePeriodTotals()
}

data class ExpensePeriodTotals(
    val today : Long,
    val thisWeek : Long,
    val thisMonth : Long
)