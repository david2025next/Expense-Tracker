package com.example.expensetracker

import androidx.core.text.isDigitsOnly

class ValidationAmount {

    fun execute(amount : String) : ValidationResult{

        if(amount.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Amount can not be empty"
            )
        }

        val isDigit = amount.isDigitsOnly()

        if(!isDigit){
            return ValidationResult(
                successful = false,
                errorMessage = "number"
            )
        }

        return ValidationResult(
            successful = true
        )

    }
}