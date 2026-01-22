package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.BalanceSummary
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceSummary @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke() : Flow<BalanceSummary> = transactionRepository.getBalanceSummary()
}