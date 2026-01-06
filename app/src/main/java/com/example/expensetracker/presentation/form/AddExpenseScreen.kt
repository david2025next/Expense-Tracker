package com.example.expensetracker.presentation.form

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.domain.model.categories
import com.example.expensetracker.utils.convertMillisToDate


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AddExpenseScreen(viewModel: AddExpenseViewModel = hiltViewModel(), backToHome: () -> Unit = {}) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBar = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.eventUiChannel, lifecycleOwner) {
        viewModel.eventUiChannel
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { event ->
            when(event) {
                UiEvent.NavigateToHome ->{
                    backToHome()
                }
                is UiEvent.ShowSnackBar ->{
                    snackBar.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Transaction",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateEvent() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            InputField(
                value = state.title,
                label = "Title",
                icon = Icons.AutoMirrored.Filled.Notes,
                error = state.errorTitle,
                onValueChange = {viewModel.uiFormEvent(FormEvent.TitleChanged(it))}
            )

            InputField(
                value = state.amount,
                isNumber = true,
                label = "Amount",
                icon = Icons.Default.AttachMoney,
                onValueChange = {viewModel.uiFormEvent(FormEvent.AmountChanged(it))},
                error = state.amountError
            )

            CategoryMenu(
                categorySelected = state.category,
                onCategorySelected = viewModel::uiFormEvent
            )

            DateTransaction(
                selectedDate = state.date,
                onDateSelected = viewModel::uiFormEvent
            )

            Button(
                onClick = { viewModel.uiFormEvent(FormEvent.Submit) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(10.dp)
            ) {
                Text(
                    text = "Save"
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTransaction(selectedDate: Long, onDateSelected: (FormEvent) -> Unit) {

    var showModal by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
    )
    Column() {
        Text(text = "Date", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = convertMillisToDate(selectedDate),
            onValueChange = {},
            textStyle = MaterialTheme.typography.bodyMedium,
            shape = RoundedCornerShape(16.dp),
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(selectedDate) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showModal = true
                        }
                    }
                }
        )

    }
    if (showModal) {
        DatePickerDialog(
            onDismissRequest = { showModal = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(
                            FormEvent.DateChanged(
                                it
                            )
                        )
                    }
                    showModal = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showModal = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryMenu(categorySelected: String, onCategorySelected: (FormEvent) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text("Category", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = categorySelected,
                textStyle = MaterialTheme.typography.bodyMedium,
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.name,
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = category.name
                            )
                        }
                    },
                    onClick = {
                        onCategorySelected(FormEvent.CategoryChanged(category.name))
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun InputField(
    value: String,
    icon: ImageVector,
    label: String,
    isNumber: Boolean = false,
    onValueChange: (String) -> Unit,
    error: String?
) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        OutlinedTextField(
            value = value,
            textStyle = MaterialTheme.typography.bodyMedium,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary
                )
            },

            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isNumber) KeyboardType.Decimal else KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth(),
            suffix = {
                if (isNumber) Text("FCFA")
                else null
            },
            shape = RoundedCornerShape(12.dp)
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}




