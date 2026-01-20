package com.example.expensetracker.domain.service

data class ValidationResult(
    val successful : Boolean,
    val errorMessage : String ? = null
)
