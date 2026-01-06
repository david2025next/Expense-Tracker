package com.example.expensetracker.presentation.home

import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.service.GetExpensePeriodTotals
import com.example.expensetracker.domain.service.GetRecentExpenses
import com.example.expensetracker.utils.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getExpensePeriodTotalsFlow: GetExpensePeriodTotals,
    private val getRecentExpensesFlow: GetRecentExpenses
) : ViewModel() {

    val uiState = getExpensePeriodTotalsFlow().combine(
        getRecentExpensesFlow()
    ) { totals, recentExpenses ->
        val recentExpenses = recentExpenses.map { it.toUi() }
        DashboardUiState(
            totals = totals.toUi(),
            recent = RecentExpenses(recentExpenses)
        )
    }.stateIn(
        viewModelScope,
        initialValue = DashboardUiState(),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "dashboardViewModel destroy")
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
    val recent: RecentExpenses = RecentExpenses()
)

data class TotalsExpensePeriod(
    val today: Long = 0,
    val thisWeek: Long = 0,
    val thisMonth: Long = 0
)

data class RecentExpenses(
    val expenses: List<ExpenseUi> = listOf()
)