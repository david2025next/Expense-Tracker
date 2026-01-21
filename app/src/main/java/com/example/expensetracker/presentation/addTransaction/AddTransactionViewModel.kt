package com.example.expensetracker.presentation.addTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Transaction
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.model.expenseCategories
import com.example.expensetracker.domain.model.incomeCategories
import com.example.expensetracker.domain.service.AddTransactionUseCase
import com.example.expensetracker.domain.service.ValidateAmount
import com.example.expensetracker.domain.service.ValidationDescription
import com.example.expensetracker.utils.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val validateAmount: ValidateAmount,
    private val validationDescription: ValidationDescription,
    private val addTransactionUseCase: AddTransactionUseCase
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

            FormEvent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {

        val descriptionResult = validationDescription(_state.value.description)
        val amountResult = validateAmount(_state.value.amount)

        val hasError = listOf(
            descriptionResult,
            amountResult
        ).any { !it.successful }

        if (hasError) {
            _state.update {
                it.copy(
                    descriptionError = descriptionResult.errorMessage,
                    amountError = amountResult.errorMessage
                )
            }
            return
        }
        viewModelScope.launch {
            addTransactionUseCase(
                Transaction(
                    amount = _state.value.amount.trim().toLong(),
                    description = _state.value.description.trim(),
                    date = _state.value.date,
                    category = _state.value.category,
                    transactionType = _state.value.transactionType
                )
            )
            _state.update { it.copy(snackBarMessage = "${state.value.transactionType.displayName} ajoute avec success") }
        }
    }

    fun resetForm(){
        _state.update{ AddTransactionUiState() }
    }
}

sealed class FormEvent {
    data class DescriptionChanged(val description: String) : FormEvent()
    data class AmountChanged(val amount: String) : FormEvent()
    data class CategoryChanged(val name: String) : FormEvent()
    data class DateChanged(val date: Long) : FormEvent()
    data class TransactionFilterChanged(val transactionType: TransactionType) :
        FormEvent()

    data object Submit : FormEvent()
}


data class AddTransactionUiState(
    val description: String = "",
    val descriptionError: String? = null,
    val amount: String = "100",
    val amountError: String? = null,
    val category: String = expenseCategories.first().name,
    val categoriesForTransaction: List<Category> = expenseCategories,
    val date: Long = LocalDate.now().toMillis(),
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val snackBarMessage : String ? = null,
)


