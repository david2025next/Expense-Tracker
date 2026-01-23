package com.example.expensetracker.presentation.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
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

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        onRegisterEvent(RegisterEvent.ImageProfile(uri))
    }
    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Register",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(40.dp))
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
            Spacer(Modifier.height(30.dp))
            InputField(
                label = "Username",
                fieldValue = registerUiState.username,
                icon = Icons.Default.Person,
                placeholder = "Enter username",
                error = null,
                onfieldInputChanged = { onRegisterEvent(RegisterEvent.UsernameChanged(it)) }
            )

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onRegisterEvent(RegisterEvent.Submit) },
                enabled = registerUiState.username.isNotBlank() && registerUiState.imageProfile != null,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Register"
                )
            }

            if (registerUiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun ProfileImagePicker(
    modifier: Modifier = Modifier,
    size : Dp = 120.dp,
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

