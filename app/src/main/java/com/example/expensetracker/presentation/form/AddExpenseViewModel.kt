package com.example.expensetracker.presentation.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.categories
import com.example.expensetracker.domain.service.AddExpenseUseCase
import com.example.expensetracker.domain.service.ValidationAmount
import com.example.expensetracker.domain.service.ValidationTitle
import com.example.expensetracker.utils.categoriesMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val validationAmount: ValidationAmount,
    private val validationTitle: ValidationTitle,
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FormState())
    val state = _state.asStateFlow()

    private val _eventUiChannel = Channel<UiEvent>()
    val eventUiChannel = _eventUiChannel.receiveAsFlow()
        .stateIn(
            viewModelScope,
            initialValue = UiEvent.Idle,
            started = SharingStarted.WhileSubscribed(5000L)
        )

    fun navigateEvent() {
        _state.update { FormState() }
        viewModelScope.launch {
            _eventUiChannel.send(UiEvent.NavigateToHome)
        }
    }

    fun uiFormEvent(event: FormEvent) {
        when (event) {
            is FormEvent.AmountChanged -> {
                _state.update { it.copy(amount = event.amount) }
            }

            is FormEvent.CategoryChanged -> {
                _state.update { it.copy(category = event.category) }
            }

            is FormEvent.DateChanged -> {
                _state.update { it.copy(date = event.date) }
            }

            is FormEvent.TitleChanged -> {
                _state.update { it.copy(title = event.title) }
            }

            FormEvent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val resultAmount = validationAmount.execute(state.value.amount)
        val resultTitle = validationTitle.execute(state.value.title)

        val hasError = listOf(
            resultTitle,
            resultAmount
        ).any { !it.successful }

        if (hasError) {
            _state.update { it.copy(errorTitle = resultTitle.errorMessage) }
            _state.update { it.copy(amountError = resultAmount.errorMessage) }
            return
        }

        viewModelScope.launch {
            addExpenseUseCase.invoke(
                Expense(
                    title = state.value.title,
                    amount = state.value.amount.toLong(),
                    date = state.value.date,
                    category = state.value.category
                )
            )
            _state.update { FormState() }
            _eventUiChannel.send(UiEvent.ShowSnackBar("Depense ajoute avec succes"))
        }
    }
}

sealed class UiEvent {
    data object Idle : UiEvent()
    data class ShowSnackBar(val message: String) : UiEvent()
    data object NavigateToHome : UiEvent()
}

data class FormState(
    val amount: String = "",
    val amountError: String? = null,
    val title: String = "",
    val errorTitle: String? = null,
    val category: String = categories.first().name,
    val date: Long = getInitialDate()
)

fun getInitialDate(): Long =
    LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()


sealed class FormEvent {
    data class AmountChanged(val amount: String) : FormEvent()
    data class DateChanged(val date: Long) : FormEvent()
    data class TitleChanged(val title: String) : FormEvent()
    data class CategoryChanged(val category: String) : FormEvent()
    data object Submit : FormEvent()
}