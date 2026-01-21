package com.example.expensetracker.data.local

import com.example.expensetracker.domain.model.Transaction
import javax.inject.Inject

class TransactionLocalDatasource @Inject constructor(
    private val transactionDao: TransactionDao
) {

    suspend fun insertTransaction(transaction: Transaction) = transactionDao.insert(transaction.toEntity())
}

private fun Transaction.toEntity() = TransactionEntity(
    description = description,
    date = date,
    transactionType = transactionType,
    category = category,
    amount = amount
)
