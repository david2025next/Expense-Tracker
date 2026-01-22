package com.example.expensetracker.utils


import java.text.NumberFormat
import java.util.Locale


fun Long.toCurrency(locale: Locale = Locale.getDefault()) : String {
    val formatter = NumberFormat.getNumberInstance(locale)
    return formatter.format(this)
}