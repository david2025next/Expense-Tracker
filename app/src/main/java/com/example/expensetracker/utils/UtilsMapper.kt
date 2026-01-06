package com.example.expensetracker.utils

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.findIconByCategoryName
import com.example.expensetracker.domain.service.ExpensePeriodTotals
import com.example.expensetracker.presentation.home.ExpenseUi
import com.example.expensetracker.presentation.home.TotalsExpensePeriod
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun ExpensePeriodTotals.toUi() = TotalsExpensePeriod(
    today = today,
    thisWeek = thisWeek,
    thisMonth = thisMonth
)

fun Expense.toUi()  = ExpenseUi(
    title = title,
    amount = amount,
    category = category,
    iconCategory = findIconByCategoryName(category),
    date = date.toHumanReadableDate()
)

fun today() = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))

fun Long.toHumanReadableDate(
    zoneId : ZoneId = ZoneId.systemDefault(),
    locale : Locale = Locale.FRENCH
): String{

    val date = Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()

    val today = LocalDate.now(zoneId)

    return when {
        date.isEqual(today) -> "Aujourd’hui"
        date.isEqual(today.minusDays(1)) -> "Hier"
        date.year == today.year ->
            date.format(DateTimeFormatter.ofPattern("d MMMM", locale))
        else ->
            date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", locale))
    }
}


data class DateRange(
    val start: Long,
    val end: Long
)

fun todayRange(zoneId: ZoneId = ZoneId.systemDefault()): DateRange {
    val today = LocalDate.now(zoneId)
    return DateRange(
        today.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        today.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
    )
}

fun weekRange(zoneId: ZoneId = ZoneId.systemDefault()): DateRange {
    val today = LocalDate.now(zoneId)
    val start = today.with(DayOfWeek.MONDAY)
    return DateRange(
        start.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        start.plusDays(7).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
    )
}

fun monthRange(zoneId: ZoneId = ZoneId.systemDefault()): DateRange {
    val today = LocalDate.now(zoneId)
    val start = today.withDayOfMonth(1)
    return DateRange(
        start.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        start.plusMonths(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
    )
}
