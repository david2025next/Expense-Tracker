package com.example.expensetracker.data

import com.example.expensetracker.data.local.TransactionLocalDatasource
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository

class TransactionRepositoryImpl constructor(
    private val transactionLocalDatasource: TransactionLocalDatasource
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction) {

    }
}