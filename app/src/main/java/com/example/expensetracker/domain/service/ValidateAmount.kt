package com.example.expensetracker.domain.service

class ValidateAmount {

    operator fun invoke(amount: String): ValidationResult {

        return try {
            val amountNumber = amount.toLong()
            if (amountNumber < 0) {
                ValidationResult(
                    successful = false,
                    errorMessage = "Amount can be negative"
                )
            } else
                ValidationResult(
                    successful = true
                )
        } catch (e: NumberFormatException) {
            ValidationResult(
                successful = false,
                errorMessage = "Invalid format Amount"
            )
        }
    }
}