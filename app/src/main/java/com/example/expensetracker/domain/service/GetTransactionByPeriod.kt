package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.Period
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import com.example.expensetracker.utils.DateRange
import com.example.expensetracker.utils.monthRange
import com.example.expensetracker.utils.todayRange
import com.example.expensetracker.utils.weekRange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionByPeriod @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(period: Period): Flow<List<Transaction>> {

        val dateRange = when (period) {
            Period.Today -> todayRange()
            Period.ThisWeek -> weekRange()
            is Period.Custom -> DateRange(period.start, period.end)
            Period.ThisMonth -> monthRange()
        }

        return transactionRepository.getTransactionByPeriod(dateRange.start, dateRange.end)
    }
}