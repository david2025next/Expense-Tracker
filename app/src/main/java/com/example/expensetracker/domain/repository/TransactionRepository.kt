package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.BalanceSummary
import com.example.expensetracker.domain.model.DailyTotals
import com.example.expensetracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun addTransaction(transaction: Transaction)
    fun getBalanceSummary(): Flow<BalanceSummary>
    fun getTotalsSpentByPeriod(start: Long, end: Long): Flow<DailyTotals>
    fun getTransactionByPeriod(start: Long, end: Long): Flow<List<Transaction>>
}