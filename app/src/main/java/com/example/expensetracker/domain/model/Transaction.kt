package com.example.expensetracker.domain.model

data class Transaction(
    val description : String,
    val amount : Long,
    val category : String,
    val date : Long,
    val transactionType: TransactionType
)
