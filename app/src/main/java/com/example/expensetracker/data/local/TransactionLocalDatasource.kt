package com.example.expensetracker.data.local

import com.example.expensetracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionLocalDatasource @Inject constructor(
    private val transactionDao: TransactionDao
) {

    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insert(transaction.toEntity())

    fun getBalanceSummary(): Flow<BalanceSummaryEntity> = transactionDao.getBalanceSummary()
    fun getTotalsSpentByPeriod(start: Long, end: Long): Flow<TransactionPeriodTotalsSpent> =
        transactionDao.getTotalsSpentPeriodTotals(start, end)

    fun getTransactionsByPeriod(start: Long, end: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionByPeriod(start, end)
}

private fun Transaction.toEntity() = TransactionEntity(
    description = description,
    date = date,
    transactionType = transactionType,
    category = category,
    amount = amount
)
