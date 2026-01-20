package com.example.expensetracker.presentation.addTransaction

import androidx.lifecycle.ViewModel
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.model.expenseCategories
import com.example.expensetracker.domain.model.incomeCategories
import com.example.expensetracker.domain.service.ValidateAmount
import com.example.expensetracker.utils.toMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class AddTransactionViewModel constructor(
    private val validateAmount: ValidateAmount = ValidateAmount()
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionUiState())
    val state = _state.asStateFlow()


    fun formEvent(event: FormEvent) {
        when (event) {
            is FormEvent.AmountChanged -> {
                _state.update { it.copy(amount = event.amount) }
            }

            is FormEvent.CategoryChanged -> {
                _state.update { it.copy(category = event.name) }
            }

            is FormEvent.DateChanged -> {
                _state.update { it.copy(date = event.date) }
            }

            is FormEvent.DescriptionChanged -> {
                _state.update { it.copy(description = event.description) }
            }

            is FormEvent.TransactionFilterChanged -> {
                when (event.transactionType) {
                    TransactionType.EXPENSE -> {
                        _state.update {
                            it.copy(
                                categoriesForTransaction = expenseCategories,
                                category = expenseCategories.first().name,
                                transactionType = event.transactionType
                            )
                        }
                    }

                    TransactionType.INCOME -> {
                        _state.update {
                            it.copy(
                                categoriesForTransaction = incomeCategories,
                                category = incomeCategories.first().name,
                                transactionType = event.transactionType
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class FormEvent {
    data class DescriptionChanged(val description: String) : FormEvent()
    data class AmountChanged(val amount: String) : FormEvent()
    data class CategoryChanged(val name: String) : FormEvent()
    data class DateChanged(val date: Long) : FormEvent()
    data class TransactionFilterChanged(val transactionType: TransactionType) :
        FormEvent()
}


data class AddTransactionUiState(
    val description: String = "",
    val descriptionError: String? = null,
    val amount: String = "100",
    val amountError: String? = null,
    val category: String = expenseCategories.first().name,
    val categoriesForTransaction: List<Category> = expenseCategories,
    val date: Long = LocalDate.now().toMillis(),
    val transactionType: TransactionType = TransactionType.EXPENSE
)


