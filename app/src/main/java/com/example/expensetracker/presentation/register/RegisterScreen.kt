package com.example.expensetracker.presentation.register

import android.R
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.example.expensetracker.presentation.addTransaction.InputField
import kotlinx.coroutines.flow.filter


@Composable
fun RegisterRouter(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    onUserRegister : ()->Unit
) {
    val state by registerViewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val currentOnUserRegister by rememberUpdatedState(onUserRegister)

    LaunchedEffect(registerViewModel, lifecycle) {
        snapshotFlow { state }
            .filter { it.isUserLoggedIn }
            .flowWithLifecycle(lifecycle)
            .collect {
                currentOnUserRegister()
                registerViewModel.reset()
            }
    }
    RegisterScreen(
        registerUiState = state,
        onRegisterEvent = registerViewModel::registerUiEvent
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerUiState: RegisterUiState,
    onRegisterEvent: (RegisterEvent) -> Unit
) {

    var passwordVisibility by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        onRegisterEvent(RegisterEvent.ImageProfile(uri))
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Bienvenue",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            ProfileImagePicker(
                modifier = Modifier.padding(bottom = 32.dp),
                imageUri = registerUiState.imageProfile,
                onClick = {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = registerUiState.username,
                    shape = MaterialTheme.shapes.small,
                    onValueChange = {onRegisterEvent(RegisterEvent.UsernameChanged(it))},
                    label = {Text("Pseudo")},
                    placeholder = {Text("Entrez votre pseudo")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = registerUiState.errorUsername !=null
                )
                Spacer(Modifier.height(4.dp))
                AnimatedVisibility(visible = registerUiState.errorUsername !=null) {
                    Text(
                        text = registerUiState.errorUsername.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = registerUiState.password,
                    shape = MaterialTheme.shapes.small,
                    onValueChange = {onRegisterEvent(RegisterEvent.PasswordChanged(it))},
                    label = {Text("Mot de passe")},
                    placeholder = {Text("Entrez votre mot de passe")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if(passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = {passwordVisibility =!passwordVisibility}
                        ) {
                            Icon(
                                imageVector = if(passwordVisibility)Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = registerUiState.errorUsername !=null
                )
                Spacer(Modifier.height(4.dp))
                AnimatedVisibility(visible = registerUiState.errorPassword !=null) {
                    Text(
                        text = registerUiState.errorPassword.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Button(
                onClick = {onRegisterEvent(RegisterEvent.Submit)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = !registerUiState.isLoading
            ) {
                if(registerUiState.isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Register")
                }
            }
        }
    }
}

@Composable
fun ProfileImagePicker(
    modifier: Modifier = Modifier,
    size : Dp = 100.dp,
    iconSize : Dp = 48.dp,
    imageUri: Uri?,
    onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Select profile image",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

