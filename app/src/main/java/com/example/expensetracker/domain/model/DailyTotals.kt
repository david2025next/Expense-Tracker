package com.example.expensetracker.domain.model

data class DailyTotals(
    val date : Long,
    val totalIncome : Long,
    val totalExpense : Long
)
