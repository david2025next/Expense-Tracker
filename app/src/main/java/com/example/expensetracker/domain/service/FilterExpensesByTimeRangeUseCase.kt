package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.utils.TimeRange
import com.example.expensetracker.utils.monthRange
import com.example.expensetracker.utils.todayRange
import com.example.expensetracker.utils.weekRange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterExpensesByTimeRangeUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {

    operator fun invoke(timeRange: TimeRange): Flow<List<Expense>> {

        return when (timeRange) {
            TimeRange.RECENT -> {
                expenseRepository.getRecentExpenses()
            }

            // use pagination for other timeRange

            TimeRange.TODAY -> {
                expenseRepository.filter(
                    todayRange().start,
                    todayRange().end
                )
            }

            TimeRange.WEEK -> {
                expenseRepository.filter(
                    weekRange().start,
                    weekRange().end
                )
            }

            TimeRange.MONTH -> {
                expenseRepository.filter(
                    monthRange().start,
                    monthRange().end
                )
            }
        }
    }
}