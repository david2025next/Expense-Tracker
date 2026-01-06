package com.example.expensetracker.domain.service

import androidx.core.text.isDigitsOnly
import javax.inject.Inject

class ValidationAmount @Inject constructor(){

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