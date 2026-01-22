package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.Period
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionByPeriod @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(period: Period): Flow<List<Transaction>>{
        TODO()
    }
}