package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) = transactionRepository.addTransaction(transaction)
}