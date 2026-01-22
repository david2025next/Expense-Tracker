package com.example.expensetracker.domain.model

sealed class Period{

    data object Today : Period()
    data object ThisWeek : Period()
    data object ThisMonth : Period()

    data class Custom(
        val start : Long,
        val end : Long
    ) : Period()
}