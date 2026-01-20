package com.example.expensetracker.presentation.addTransaction

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
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
//                val result = validateAmount(event.amount)
//                if(!result.successful){
//                    _state.update { it.copy(amountError = result.errorMessage,) }
//                }
//                else{
//                    _state.update { it.copy(amount = event.amount.toLong(), amountError = null) }
//                }
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
                when (event.transactionFilterSelector) {
                    TransactionFilterSelector.EXPENSE -> {
                        _state.update { it.copy(categoriesForTransaction = expenseCategories, category = expenseCategories.first().name) }
                    }

                    TransactionFilterSelector.INCOME -> {
                        _state.update { it.copy(categoriesForTransaction = incomeCategories, category = incomeCategories.first().name) }
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
    data class TransactionFilterChanged(val transactionFilterSelector: TransactionFilterSelector) :
        FormEvent()
}


data class AddTransactionUiState(
    val description: String = "",
    val descriptionError: String? = null,
    val amount: Long = 50L,
    val amountError: String? = null,
    val category: String = expenseCategories.first().name,
    val categoriesForTransaction: List<Category> = listOf(),
    val date: Long = LocalDate.now().toMillis()
)

data class Category(
    val icon: ImageVector,
    val name: String
)

val expenseCategories = listOf(
    Category(
        icon = Icons.Default.Restaurant,
        name = "Alimentation"
    ),
    Category(
        icon = Icons.Default.Wifi,
        name = "Internet"
    )
)

private val incomeCategories = listOf(
    Category(
        icon = Icons.Default.Movie,
        name = "Movie"
    ),
    Category(
        icon = Icons.Default.Work,
        name = "Work"
    )
)