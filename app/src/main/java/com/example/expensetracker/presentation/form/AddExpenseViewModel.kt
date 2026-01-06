package com.example.expensetracker.presentation.form

import androidx.lifecycle.ViewModel
import com.example.expensetracker.domain.service.ValidationAmount
import com.example.expensetracker.utils.categoriesMenu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.ZoneId

class AddExpenseViewModel(
    private val validationAmount: ValidationAmount = ValidationAmount()
) : ViewModel() {

    private val _state = MutableStateFlow(FormState())
    val state = _state.asStateFlow()

    fun uiEvent(event: FormEvent){
        when(event) {
            is FormEvent.AmountChanged ->{
                _state.update { it.copy(amount = event.amount) }
            }
            is FormEvent.CategoryChanged ->{
                _state.update { it.copy(category = event.category) }
            }
            is FormEvent.DateChanged -> {
                _state.update { it.copy(date = event.date) }
            }
            is FormEvent.DescriptionChanged ->{
                _state.update { it.copy(description = event.description) }
            }
            is FormEvent.Submit ->{
                submit()
            }
        }
    }

    private fun submit(){
        val result = validationAmount.execute(state.value.amount)
        if(!result.successful){
            _state.update { it.copy(amountError = result.errorMessage) }
            return
        }
    }
}

data class FormState(
    val amount : String = "",
    val amountError : String ? = null,
    val description : String = "",
    val category : String = categoriesMenu.first().name,
    val date : Long = getInitialDate()
)

fun getInitialDate() : Long = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

sealed class FormEvent{
    data class AmountChanged(val amount : String) : FormEvent()
    data class DateChanged(val date : Long) : FormEvent()
    data class DescriptionChanged(val description : String) : FormEvent()
    data class CategoryChanged(val category : String) : FormEvent()
    data object Submit : FormEvent()
}