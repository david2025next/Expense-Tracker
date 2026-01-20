package com.example.expensetracker.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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