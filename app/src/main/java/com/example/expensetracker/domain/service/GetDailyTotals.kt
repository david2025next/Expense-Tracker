package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.DailyTotals
import com.example.expensetracker.domain.repository.TransactionRepository
import com.example.expensetracker.utils.todayRange
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetDailyTotals @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(date : LocalDate = LocalDate.now()) : Flow<DailyTotals> {

        val (start, end) = todayRange()
        return transactionRepository.getTotalsSpentByPeriod(
            start, end
        )
    }
}