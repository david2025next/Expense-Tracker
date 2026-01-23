package com.example.expensetracker.presentation.register

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun registerUiEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.ImageProfile -> _state.update { it.copy(imageProfile = event.imageProfile) }
            RegisterEvent.Submit -> submit()
            is RegisterEvent.UsernameChanged -> _state.update { it.copy(username = event.username) }
            is RegisterEvent.PasswordChanged -> _state.update { it.copy(password = event.password) }
        }
    }
    fun reset(){
        _state.update { it.copy(isUserLoggedIn = false) }
    }

    private fun submit() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val fileURI = copyProfileImage(_state.value.imageProfile!!)
            userPreferences.savePreference(_state.value.username, fileURI, true)
            _state.update { it.copy(isLoading = false, isUserLoggedIn = true) }

        }
    }

    private suspend fun copyProfileImage(uri: Uri): URI {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "profile_image.jpg")
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return@withContext file.toURI()
        }
    }
}

sealed class RegisterEvent {
    data class UsernameChanged(val username: String) : RegisterEvent()
    data class ImageProfile(val imageProfile: Uri?) : RegisterEvent()
    data class PasswordChanged(val password : String) : RegisterEvent()
    data object Submit : RegisterEvent()
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val imageProfile: Uri? = null,
    val username: String = "",
    val errorUsername : String ? = null,
    val password : String = "",
    val errorPassword : String ? = null,
    val isUserLoggedIn : Boolean = false
)