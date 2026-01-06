package com.example.expensetracker

data class ValidationResult(
    val errorMessage : String ? = null,
    val successful : Boolean
)
