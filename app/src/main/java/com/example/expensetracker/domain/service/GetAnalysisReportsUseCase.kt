package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.presentation.statistics.StatisticPeriod
import com.example.expensetracker.utils.getInitialDate
import com.example.expensetracker.utils.monthRange
import com.example.expensetracker.utils.weekRange
import com.example.expensetracker.utils.yearRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.exp

class GetAnalysisReportsUseCase @Inject constructor(
    private val getExpenseBetweenUseCase: GetExpenseBetweenUseCase
) {

    operator fun invoke(statisticPeriod: StatisticPeriod): Flow<AnalysisReports> {

        val (start, end) = when (statisticPeriod) {
            StatisticPeriod.WEEK -> weekRange()
            StatisticPeriod.MONTH -> monthRange()
            StatisticPeriod.YEAR -> yearRange()
        }

        return getExpenseBetweenUseCase(start, end)
            .map { expenses ->

                val totalAmount = expenses.sumOf { it.amount }
                val topCategories = getTopCategories(totalAmount, expenses)
                val dataChart = getDataChart(totalAmount, statisticPeriod, expenses)

                AnalysisReports(
                    topCategories = topCategories,
                    dataChart = dataChart
                )
            }
    }

    private fun getDataChart(
        totalAmount: Long,
        statisticPeriod: StatisticPeriod,
        expenses: List<Expense>
    ): Map<String, Float> {

        val dataChart = initializeData(statisticPeriod).toMutableMap()

        expenses
            .groupBy {
                when (statisticPeriod) {
                    StatisticPeriod.WEEK -> it.date.toInt()
                    StatisticPeriod.MONTH -> { it.date.toInt() }
                    StatisticPeriod.YEAR -> {
                        Instant.ofEpochMilli(it.date)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .month
                            .value
                    }
                }

            }.map{(date, expensesForThisDate) ->
                val totalsAmountForExpenseForThisDate = expensesForThisDate.sumOf { it.amount }
                val formatDate = formatDataWithFilterPeriod(date.toLong(), statisticPeriod)
                DataReports(
                    period = formatDate,
                    percent = (totalsAmountForExpenseForThisDate / totalAmount.toFloat())
                )
            }.forEach { reports ->
                dataChart[reports.period] = reports.percent
            }
        return dataChart
    }

    private fun formatDataWithFilterPeriod(
        date: Long,
        statisticPeriod: StatisticPeriod,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): String {
        val day = Instant.ofEpochMilli(date)
            .atZone(zoneId)
            .toLocalDate()

        return when (statisticPeriod) {
            StatisticPeriod.WEEK -> {
                formatDateWeek(day)
            }

            StatisticPeriod.MONTH -> {
                formatDateMonth(day)
            }

            StatisticPeriod.YEAR -> {
                formatDateYear(day)
            }
        }
    }

    private fun formatDateYear(day: LocalDate): String {
        return when(day.month) {
            Month.APRIL -> "Avr"
            Month.AUGUST -> "Aout"
            Month.DECEMBER -> "Dec"
            Month.FEBRUARY -> "Fev"
            Month.JANUARY -> "Jan"
            Month.JULY -> "Juil"
            Month.JUNE -> "Juin"
            Month.MARCH -> "Mars"
            Month.MAY -> "Mai"
            Month.NOVEMBER -> "Nov"
            Month.OCTOBER -> "Oct"
            Month.SEPTEMBER -> "Sept"
        }
    }

    private fun formatDateMonth(day: LocalDate): String {
        return formatDateYear(day)
    }

    private fun formatDateWeek(day: LocalDate): String {
        return when(day.dayOfWeek) {
            DayOfWeek.FRIDAY -> "Ven"
            DayOfWeek.MONDAY -> "Lun"
            DayOfWeek.SATURDAY -> "Sam"
            DayOfWeek.SUNDAY -> "Dim"
            DayOfWeek.THURSDAY -> "Jeu"
            DayOfWeek.TUESDAY -> "Mar"
            DayOfWeek.WEDNESDAY -> "Mer"
        }
    }

    private fun initializeData(statisticPeriod: StatisticPeriod): Map<String, Float> =
        when (statisticPeriod) {
            StatisticPeriod.WEEK -> getInitialDataForWeek()
            StatisticPeriod.MONTH -> getInitialDataForMonth()
            StatisticPeriod.YEAR -> getInitialDataForYear()
        }

    private fun getInitialDataForYear() = mutableMapOf(
        "Jan" to 0.0f,
        "Fev" to 0.0f,
        "Mars" to 0.0f,
        "Avr" to 0.0f,
        "Mai" to 0.0f,
        "Juin" to 0.0f,
        "Juil" to 0.0f,
        "Aout" to 0.0f,
        "Sept" to 0.0f,
        "Oct" to 0.0f,
        "Nov" to 0.0f,
        "Dec" to 0.0f,
    )

    private fun getInitialDataForMonth() =
        mutableMapOf("" to 0.0f)

    private fun getInitialDataForWeek() = mutableMapOf(
        "Lun" to 0.0f,
        "Mar" to 0.0f,
        "Mer" to 0.0f,
        "Jeu" to 0.0f,
        "Ven" to 0.0f,
        "Sam" to 0.0f,
        "Dim" to 0.0f
    )

    private fun getTopCategories(totalAmount: Long, expenses: List<Expense>, count: Int = 3) =
        expenses.groupBy { it.category }
            .map { (categoryName, expensesForCategory) ->
                val totalAmountForExpenseInThisCategory = expensesForCategory.sumOf { it.amount }
                TopCategoryItem(
                    name = categoryName,
                    percent = (totalAmountForExpenseInThisCategory / totalAmount.toFloat())
                )
            }
            .take(count)
}


data class AnalysisReports(
    val topCategories: List<TopCategoryItem>,
    val dataChart: Map<String, Float>
)

data class TopCategoryItem(
    val name: String,
    val percent: Float
)

data class DataReports(
    val period: String,
    val percent: Float
)