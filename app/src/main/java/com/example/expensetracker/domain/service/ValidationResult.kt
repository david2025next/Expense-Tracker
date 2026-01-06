package com.example.expensetracker.domain.service

data class ValidationResult(
    val errorMessage : String ? = null,
    val successful : Boolean
)
