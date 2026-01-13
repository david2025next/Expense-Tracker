package com.example.expensetracker.presentation.statistics

import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.findIconByCategoryName
import com.example.expensetracker.domain.service.CategoryPercentage
import com.example.expensetracker.domain.service.GetAnalysisReportsUseCase
import com.example.expensetracker.domain.service.TopCategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val getAnalysisReportsUseCase: GetAnalysisReportsUseCase
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(StatisticPeriod.WEEK)

    @OptIn(ExperimentalCoroutinesApi::class)

    val state = selectedPeriod.flatMapLatest { period ->
        getAnalysisReportsUseCase(period)
    }.map { analysisReports ->
        val topCategoriesUi = analysisReports.topCategories.map { item -> item.toUi() }
        AnalysisUiState(
            topCategories = topCategoriesUi,
            dataCharts = analysisReports.dataChart,
            period = selectedPeriod.value
        )
    }.stateIn(
        viewModelScope,
        initialValue = AnalysisUiState(),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onSelectedPeriodChanged(period: StatisticPeriod) {
        selectedPeriod.update { period }
    }
}


private fun TopCategoryItem.toUi() = AnalysisCategoryUiState(
    name = name,
    icon = findIconByCategoryName(name),
    percent = percent
)

data class AnalysisUiState(
    val period: StatisticPeriod = StatisticPeriod.WEEK,
    val topCategories: List<AnalysisCategoryUiState> = listOf(),
    val dataCharts: Map<String, Float> = mapOf(),
)

data class AnalysisCategoryUiState(
    val name: String,
    val icon: ImageVector,
    val percent: Float
)

enum class StatisticPeriod(val displayName: String) {
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year")
}