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
        .flatMapLatest { range -> filterExpensesByTimeRangeUseCase(range) }
        .map { expenses -> expenses.map { it.toUi() } }
        .combine(getExpensePeriodTotalsFlow()) { expenses, expensePeriodTotals ->
            DashboardUiState(
                spentToday = expensePeriodTotals.today,
                spentThisWeek = expensePeriodTotals.thisWeek,
                spentThisMonth = expensePeriodTotals.thisMonth,
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


data class ExpenseItemUiState(
    val title: String,
    val amount: Long,
    val createdAt: String,
    val iconCategory: ImageVector,
    val category: String
)

data class DashboardUiState(
    val spentToday : Long = 0,
    val spentThisWeek : Long = 0,
    val spentThisMonth : Long = 0,
    val expenses: List<ExpenseItemUiState> = listOf()
)
