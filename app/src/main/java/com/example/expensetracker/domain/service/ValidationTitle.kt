package com.example.expensetracker.domain.service

import androidx.core.text.isDigitsOnly

class ValidationTitle {

    fun execute(title : String) : ValidationResult{
        if(title.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Le titre ne peut etre vide"
            )
        }
        if(title.isDigitsOnly()){
            return ValidationResult(
                successful = false,
                errorMessage = "Titre invalide"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}