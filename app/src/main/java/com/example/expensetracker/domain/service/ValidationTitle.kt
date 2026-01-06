package com.example.expensetracker.domain.service

import androidx.core.text.isDigitsOnly
import javax.inject.Inject

class ValidationTitle @Inject constructor() {

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