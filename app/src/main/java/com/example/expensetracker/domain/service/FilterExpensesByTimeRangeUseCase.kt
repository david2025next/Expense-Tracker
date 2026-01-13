package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.utils.TimeRange
import com.example.expensetracker.utils.monthRange
import com.example.expensetracker.utils.todayRange
import com.example.expensetracker.utils.weekRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class FilterExpensesByTimeRangeUseCase @Inject constructor(
    private val getExpenseBetweenUseCase: GetExpenseBetweenUseCase
) {

    operator fun invoke(timeRange: TimeRange): Flow<List<Expense>> {

        return when (timeRange) {
            TimeRange.RECENT -> {
                getExpenseBetweenUseCase(start = todayRange().start, end = todayRange().end)
                    .take(4)
            }


            TimeRange.TODAY -> {
                getExpenseBetweenUseCase(
                    start = todayRange().start,
                    end = todayRange().end
                )
            }

            TimeRange.WEEK -> {
                getExpenseBetweenUseCase(
                    start = weekRange().start,
                    end = weekRange().end
                )
            }

            TimeRange.MONTH -> {
                getExpenseBetweenUseCase(
                    start = monthRange().start,
                    end = monthRange().end
                )
            }
        }
    }
}