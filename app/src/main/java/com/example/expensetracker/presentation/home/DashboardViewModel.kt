package com.example.expensetracker.presentation.home

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.service.FilterExpensesByTimeRangeUseCase
import com.example.expensetracker.domain.service.GetExpensePeriodTotals
import com.example.expensetracker.utils.TimeRange
import com.example.expensetracker.utils.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getExpensePeriodTotalsFlow: GetExpensePeriodTotals,
    private val filterExpensesByTimeRangeUseCase: FilterExpensesByTimeRangeUseCase
) : ViewModel() {

    private val _selectedTimeRange = MutableStateFlow(TimeRange.RECENT)
    val selectedTimeRange = _selectedTimeRange.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = selectedTimeRange
        .flatMapLatest { range ->
            filterExpensesByTimeRangeUseCase(range)
        }
        .map { expenses ->
            expenses.map { it.toUi() }
        }
        .combine(getExpensePeriodTotalsFlow()) { expenses, expensePeriodTotals ->
            DashboardUiState(
                totals = TotalsExpensePeriod(
                    today = expensePeriodTotals.today,
                    thisMonth = expensePeriodTotals.thisMonth,
                    thisWeek = expensePeriodTotals.thisWeek
                ),
                expenses = expenses
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            DashboardUiState()
        )

    fun onFilterChanged(timeRange: TimeRange) {
        _selectedTimeRange.update { timeRange }
    }
}


data class ExpenseUi(
    val title: String,
    val amount: Long,
    val date: String,
    val iconCategory: ImageVector,
    val category: String
)

data class DashboardUiState(
    val totals: TotalsExpensePeriod = TotalsExpensePeriod(),
    val expenses: List<ExpenseUi> = listOf()
)

data class TotalsExpensePeriod(
    val today: Long = 0,
    val thisWeek: Long = 0,
    val thisMonth: Long = 0
)
