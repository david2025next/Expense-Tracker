package com.example.expensetracker.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun Long.toHumanDate(
    locale: Locale = Locale.FRENCH,
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val date = Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()

    return DateTimeFormatter.ofPattern("d MMM yyyy", locale).format(date)

}

fun LocalDate.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long = this
    .atStartOfDay(zoneId)
    .toInstant()
    .toEpochMilli()


fun humanReadableDateWeek(
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.FRENCH,
    date: Long
): String {
    return Instant.ofEpochMilli(date)
        .atZone(zoneId)
        .dayOfWeek
        .getDisplayName(TextStyle.FULL, locale)
}

fun humanReadableDateMonth(
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.FRENCH,
    date: Long
): String {

    val date = Instant.ofEpochMilli(date)
        .atZone(zoneId)
        .toLocalDate()
    val today = LocalDate.now()
    return when{
        date.isEqual(today.minusDays(1)) -> "Hier"
        date.isEqual(today) -> "Aujourd'hui"
        else -> date.format(DateTimeFormatter.ofPattern("d MMMM", locale))
    }
}

fun humanReadableDateCustom(
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.FRENCH,
    date: Long
): String {
    val date = Instant.ofEpochMilli(date)
        .atZone(zoneId)
        .toLocalDate()
    return date.format(DateTimeFormatter.ofPattern("d MMMM", locale))
}