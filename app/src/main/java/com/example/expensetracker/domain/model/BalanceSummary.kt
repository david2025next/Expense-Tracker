package com.example.expensetracker.domain.model

data class BalanceSummary(
    val totalIncome: Long,
    val totalExpense: Long,
) {
    val balance: Long
        get() = totalIncome - totalExpense

    val percent: Float
        get() = 1 - balance /  totalIncome.toFloat()
}
