package com.example.expensetracker.utils


import java.text.NumberFormat
import java.util.Locale


fun Long.toCurrency(locale: Locale = Locale.getDefault()) : String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(this)
}