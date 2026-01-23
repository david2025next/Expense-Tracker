package com.example.expensetracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.datastore.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    val loginState: StateFlow<LoginState> = userPreferences.isLogged()
        .map { value ->
            Log.d("AUTH", "value datastore $value")
            if (value) LoginState.LoggedIn
            else LoginState.NotLoggedIn
        }
        .stateIn(
            viewModelScope,
            initialValue = LoginState.LOADING,
            started = SharingStarted.Eagerly
        )
}

enum class LoginState {
    LoggedIn,
    NotLoggedIn,
    LOADING
}