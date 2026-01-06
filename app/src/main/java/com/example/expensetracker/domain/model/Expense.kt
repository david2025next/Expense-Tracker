package com.example.expensetracker.domain.model

data class Expense(
    val title : String,
    val amount : Long,
    val date : Long,
    val category : Category
)
