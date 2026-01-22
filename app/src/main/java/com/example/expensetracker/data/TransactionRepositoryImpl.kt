package com.example.expensetracker.data

import com.example.expensetracker.data.local.BalanceSummaryEntity
import com.example.expensetracker.data.local.TransactionEntity
import com.example.expensetracker.data.local.TransactionLocalDatasource
import com.example.expensetracker.data.local.TransactionPeriodTotalsSpent
import com.example.expensetracker.domain.model.BalanceSummary
import com.example.expensetracker.domain.model.DailyTotals
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionLocalDatasource: TransactionLocalDatasource
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction) =
        transactionLocalDatasource.insertTransaction(transaction)

    override fun getBalanceSummary(): Flow<BalanceSummary> =
        transactionLocalDatasource.getBalanceSummary().map { it.toDomain() }

    override fun getTotalsSpentByPeriod(
        start: Long,
        end: Long
    ): Flow<DailyTotals> =
        transactionLocalDatasource.getTotalsSpentByPeriod(start, end).map { it.toDomain() }

    override fun getTransactionByPeriod(
        start: Long,
        end: Long
    ): Flow<List<Transaction>> = transactionLocalDatasource.getTransactionsByPeriod(start, end)
        .map { transactions -> transactions.map { it.toDomain() } }
}


private fun TransactionEntity.toDomain() = Transaction(
    description = description,
    amount = amount,
    category = category,
    date = date,
    transactionType = transactionType
)

private fun TransactionPeriodTotalsSpent.toDomain() = DailyTotals(
    totalIncome = totalsIncome,
    totalExpense = totalsExpense,
)

private fun BalanceSummaryEntity.toDomain() = BalanceSummary(
    totalIncome = totalsIncome,
    totalExpense = totalsExpense
)
