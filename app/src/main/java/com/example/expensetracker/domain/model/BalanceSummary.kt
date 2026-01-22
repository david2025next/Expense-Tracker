package com.example.expensetracker.domain.model

data class BalanceSummary(
    val totalIncome : Long,
    val totalExpense :Long,
){
    val balance : Long
        get() = totalIncome-totalExpense
}
