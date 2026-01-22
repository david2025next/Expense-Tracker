package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.exception.InsufficientFoundError
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        val balanceSummary = transactionRepository.getBalanceSummary().first()

        if (transaction.transactionType == TransactionType.EXPENSE && (balanceSummary.balance - transaction.amount < 0)) {
            return Result.failure(
                InsufficientFoundError("Fond Insuffisant pour effectuer cette depense")
            )
        }
        transactionRepository.addTransaction(transaction)
        return Result.success(Unit)
    }
}