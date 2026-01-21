package com.example.expensetracker.domain.service

import androidx.core.text.isDigitsOnly
import javax.inject.Inject

class ValidationDescription @Inject constructor() {

    operator fun invoke(description: String): ValidationResult {

        if (description.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Veuillez saisir une description"
            )
        }
        if (description.isDigitsOnly()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La description doit contenir du texte aussi"
            )
        }
        if (description.trim().length > 100) {
            return ValidationResult(
                successful = false,
                errorMessage = "La description ne peut depasser 100 caracteres"
            )
        }
        if (description.trim().length < 3) {
            return ValidationResult(
                successful = false,
                errorMessage = "La description doit contenir au moins 3 caracteres"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}