package com.example.expensetracker.presentation.form

import androidx.lifecycle.ViewModel
import com.example.expensetracker.domain.service.ValidationAmount
import com.example.expensetracker.domain.service.ValidationTitle
import com.example.expensetracker.utils.categoriesMenu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.ZoneId

class AddExpenseViewModel(
    private val validationAmount: ValidationAmount = ValidationAmount(),
    private val validationTitle: ValidationTitle = ValidationTitle()
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
            is FormEvent.TitleChanged ->{
                _state.update { it.copy(title = event.title) }
            }
            is FormEvent.Submit ->{
                submit()
            }
        }
    }

    private fun submit(){
        val resultAmount = validationAmount.execute(state.value.amount)
        val resultTitle = validationTitle.execute(state.value.title)

        val hasError = listOf(
            resultTitle,
            resultAmount
        ).any { !it.successful }

        if(hasError){
            _state.update { it.copy(errorTitle = resultTitle.errorMessage) }
            _state.update { it.copy(amountError = resultAmount.errorMessage) }
            return
        }
    }
}

data class FormState(
    val amount : String = "",
    val amountError : String ? = null,
    val title : String = "",
    val errorTitle : String ? = null,
    val category : String = categoriesMenu.first().name,
    val date : Long = getInitialDate()
)

fun getInitialDate() : Long = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

sealed class FormEvent{
    data class AmountChanged(val amount : String) : FormEvent()
    data class DateChanged(val date : Long) : FormEvent()
    data class TitleChanged(val title : String) : FormEvent()
    data class CategoryChanged(val category : String) : FormEvent()
    data object Submit : FormEvent()
}