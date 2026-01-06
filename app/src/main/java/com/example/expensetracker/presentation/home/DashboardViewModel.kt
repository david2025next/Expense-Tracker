package com.example.expensetracker.presentation.home

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()
}

data class ExpenseUi(
    val title : String,
    val amount : Long,
    val date : String,
    val iconCategory : ImageVector,
    val category : String
)

data class DashboardState(
    val totalExpenseThisMonth : Long = 0,
    val totalExpenseThisWeek: Long = 0,
    val totalExpenseToday : Long = 0,
    val lastFourExpenses : List<ExpenseUi> = listOf()
)