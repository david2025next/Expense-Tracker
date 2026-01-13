package com.example.expensetracker.presentation.statistics

import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.findIconByCategoryName
import com.example.expensetracker.domain.service.CategoryPercentage
import com.example.expensetracker.domain.service.GetTopCategoriesExpenseByPeriodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor (
    private val getTopCategoriesExpense  : GetTopCategoriesExpenseByPeriodUseCase
): ViewModel() {


    private val selectedFilterPeriod = MutableStateFlow(FilterPeriod.WEEK)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state : StateFlow<AnalysisUiState> = selectedFilterPeriod
        .flatMapLatest { filterPeriod -> getTopCategoriesExpense(filterPeriod) }
        .map { categoryPercentages ->
            Log.d("TAG", "analyisViewModl: $categoryPercentages ")
            AnalysisUiState(
                period = selectedFilterPeriod.value,
                topCategories = categoryPercentages.map { it.toUi() }
            )
        }
        .stateIn(
            viewModelScope,
            initialValue = AnalysisUiState(),
            started = SharingStarted.WhileSubscribed(5000L)
        )

    fun onSelectedFilterPeriodChange(filterPeriod: FilterPeriod){
        selectedFilterPeriod.update { filterPeriod }
    }
}

private fun CategoryPercentage.toUi(): AnalysisCategoryUiState  = AnalysisCategoryUiState(
    name = categoryName,
    icon = findIconByCategoryName(categoryName),
    percent = percentage
)


data class AnalysisUiState(
    val period: FilterPeriod = FilterPeriod.WEEK,
    val topCategories : List<AnalysisCategoryUiState> = listOf()
)

data class AnalysisCategoryUiState(
    val name : String,
    val icon : ImageVector,
    val percent : Float
)

enum class FilterPeriod {
    WEEK,
    MONTH,
    YEAR
}