package com.example.expensetracker.domain.service

import android.util.Log
import com.example.expensetracker.presentation.statistics.StatisticPeriod
import com.example.expensetracker.utils.TimeRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTopCategoriesExpenseByPeriodUseCase @Inject constructor(
    private val filterExpensesByTimeRangeUseCase: FilterExpensesByTimeRangeUseCase
) {

    operator fun invoke(
        statisticPeriod: StatisticPeriod,
        count: Int = 3
    ): Flow<List<CategoryPercentage>> {
        return filterExpensesByTimeRangeUseCase(TimeRange.valueOf(statisticPeriod.name))
            .map { expenses ->
                val totalAmount = expenses.sumOf { it.amount }
                Log.d("TAG", "getCategoriesUseCase: $totalAmount ")
                if (totalAmount == 0L) return@map emptyList()
                Log.d("TAG", "getCategoriesUseCase: where")
                expenses
                    .groupBy { it.category }
                    .map { (categoryName, expensesInCategory) ->
                        val categoryTotal = expensesInCategory.sumOf { it.amount }

                        CategoryPercentage(
                            categoryName = categoryName,
                            percentage = (categoryTotal / totalAmount.toFloat())
                        )
                    }
                    .sortedByDescending { it.percentage }
                    .take(count)
            }
    }
}

data class CategoryPercentage(
    val categoryName: String,
    val percentage: Float
)
