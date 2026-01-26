package com.example.expensetracker.presentation.home

import android.net.Uri
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.datastore.UserPreferences
import com.example.expensetracker.domain.model.Period
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.model.findIconByCategoryName
import com.example.expensetracker.domain.service.GetBalanceSummary
import com.example.expensetracker.domain.service.GetDailyTotals
import com.example.expensetracker.domain.service.GetTransactionByPeriod
import com.example.expensetracker.ui.navigation.Screen
import com.example.expensetracker.utils.humanReadableDateCustom
import com.example.expensetracker.utils.humanReadableDateMonth
import com.example.expensetracker.utils.humanReadableDateWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getBalanceSummary: GetBalanceSummary,
    private val getTransactionByPeriod: GetTransactionByPeriod,
    userPreferences: UserPreferences
) : ViewModel() {

    val userInfo = userPreferences.getUsername()
        .combine(userPreferences.getImageProfile()) { username, imageProfile ->
            UserInfo(
                username = username,
                imageProfile = imageProfile
            )
        }
        .stateIn(
            viewModelScope,
            initialValue = UserInfo(),
            started = SharingStarted.WhileSubscribed(5000L)
        )
    private val _selectedPeriod = MutableStateFlow<Period>(Period.Today)

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state
        .stateIn(
            viewModelScope,
            initialValue = HomeUiState(isLoading = true),
            started = SharingStarted.WhileSubscribed(5_000L)
        )

    init {
        viewModelScope.launch {
            getBalanceSummary()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .combine(getTransactionByPeriod(_selectedPeriod.value)) { balanceAndPercent, trs ->
                    val transactionsUi = trs.map { it.toUi() }
                    _state.update { it.copy(isLoading = false) }
                    _state.update {
                        it.copy(
                            balance = balanceAndPercent.balance,
                            percent = balanceAndPercent.percent,
                            transactions = transactionsUi,
                            totalsSpentForPeriod = transactionsUi.calculateTotalExpense()
                        )
                    }
                }
                .collect()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactions = _selectedPeriod
        .flatMapLatest { newPeriod -> getTransactionByPeriod(newPeriod) }
        .onEach { trs -> _state.update { it.copy(transactions = trs.map { it.toUi() }) } }

    fun selectedPeriodChanged(periodRange: PeriodRange) {
        _state.update { it.copy(period = periodRange) }
        when (periodRange) {
            PeriodRange.TODAY -> _selectedPeriod.update { Period.Today }
            PeriodRange.WEEK -> _selectedPeriod.update { Period.ThisWeek }
            PeriodRange.MONTH -> _selectedPeriod.update { Period.ThisMonth }
        }
    }

    private fun Transaction.toUi() = TransactionItemUState(
        description = description,
        amount = amount,
        category = category,
        isExpense = TransactionType.EXPENSE == transactionType,
        icon = findIconByCategoryName(category),
        displayDate = formatDate(_selectedPeriod.value, date)
    )
}

private fun List<TransactionItemUState>.calculateTotalExpense() =
    this.filter { it.isExpense }.sumOf { it.amount }

private fun formatDate(period: Period, date: Long): String? {

    return when (period) {
        is Period.Custom -> humanReadableDateCustom(date = date)
        Period.ThisMonth -> humanReadableDateMonth(date = date)
        Period.ThisWeek -> humanReadableDateWeek(date = date)
        Period.Today -> null
    }
}


data class BalanceSummaryAndDailyTotalsUiState(
    val balance: Long = 0,
    val income: Long = 0,
    val expense: Long = 0,
    val totalSpentTodayIncome: Long = 0,
    val totalSpentTodayExpense: Long = 0,

)

data class HomeUiState(
    val balance: Long = 0,
    val percent: Float = 0f,
    val totalsSpentForPeriod: Long = 0,
    val transactions: List<TransactionItemUState> = listOf(),
    val isLoading: Boolean = false,
    val period : PeriodRange = PeriodRange.TODAY
)

data class TransactionItemUState(
    val icon: ImageVector,
    val description: String,
    val category: String,
    val amount: Long,
    val isExpense: Boolean,
    val displayDate: String? = null
)

enum class PeriodRange(val displayName: String) {
    TODAY("Aujourd'hui"),
    WEEK("Semaine"),
    MONTH("Mois"),
    //CALENDAR("Calendrier")
}

data class UserInfo(
    val username: String = "",
    val imageProfile: Uri? = null
)