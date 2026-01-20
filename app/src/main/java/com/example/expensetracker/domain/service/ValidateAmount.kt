package com.example.expensetracker.domain.service

class ValidateAmount {

    operator fun invoke(amount: String): ValidationResult {

        if (amount.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Veuillez saisir un montant"
            )
        }
        return try {
            val amountNumber = amount.trim().toLong()
            if (amountNumber < 0) {
                ValidationResult(
                    successful = false,
                    errorMessage = "Le montant ne peut etre negatif"
                )
            }
            else if(amountNumber==0L){
                ValidationResult(
                    successful = false,
                    errorMessage = "Le montant doit etre superieur a 0"
                )
            }
            else if (amountNumber > 1_000_000_000) {
                ValidationResult(
                    successful = false,
                    errorMessage = "Montant trop eleve"
                )
            } else {
                ValidationResult(
                    successful = true
                )
            }
        } catch (e: NumberFormatException) {
            ValidationResult(
                successful = false,
                errorMessage = "Le montant saisi n'est pas valide"
            )
        }
    }
}