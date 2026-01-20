package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Transaction

interface TransactionRepository {

    suspend fun addTransaction(transaction: Transaction)
}