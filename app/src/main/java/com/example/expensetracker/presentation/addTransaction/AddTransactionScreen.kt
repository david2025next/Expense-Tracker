package com.example.expensetracker.presentation.addTransaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.utils.toHumanDate

@Composable
fun AddTransactionRoute(
    addTransactionViewModel: AddTransactionViewModel = hiltViewModel(),
    goToHome: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    AddTransactionScreen(
        snackBarHostState = snackBarHostState,
        addTransactionViewModel = addTransactionViewModel,
        goToHome = goToHome
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionScreen(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    addTransactionViewModel: AddTransactionViewModel = viewModel(),
    goToHome: () -> Unit
) {

    val state by addTransactionViewModel.state.collectAsStateWithLifecycle()

    state.snackBarMessage?.let { message ->
        LaunchedEffect(state) {
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            addTransactionViewModel.resetForm()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ajouter une transaction",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = goToHome
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TransactionFilterSelector {
                addTransactionViewModel.formEvent(
                    FormEvent.TransactionFilterChanged(
                        it
                    )
                )
            }
            Spacer(Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InputField(
                    label = "Description",
                    fieldValue = state.description,
                    icon = Icons.AutoMirrored.Filled.Notes,
                    error = state.descriptionError
                ) { addTransactionViewModel.formEvent(FormEvent.DescriptionChanged(it)) }

                InputField(
                    label = "Amount",
                    fieldValue = state.amount,
                    icon = Icons.Default.AttachMoney,
                    error = state.amountError,
                    isNumber = true
                ) { addTransactionViewModel.formEvent(FormEvent.AmountChanged(it)) }

                CategoryField(
                    selectedCategory = state.category,
                    categories = state.categoriesForTransaction
                ) { addTransactionViewModel.formEvent(FormEvent.CategoryChanged(it)) }

                DateTransaction(
                    date = state.date
                ) { addTransactionViewModel.formEvent(FormEvent.DateChanged(it)) }
            }

            Button(
                onClick = { addTransactionViewModel.formEvent(FormEvent.Submit) },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp)
            ) {
                Text(text = "AJOUTER", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun TransactionFilterSelector(onTransactionFilterChanged: (TransactionType) -> Unit) {

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val borderColor = MaterialTheme.colorScheme.outlineVariant


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(containerColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TransactionType.entries.forEachIndexed { index, transaction ->
            val isSelected = selectedIndex == index

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                animationSpec = tween(300),
                label = "bgAnim"
            )

            val contentColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "textAnim"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor)
                    .clickable {
                        selectedIndex = index
                        onTransactionFilterChanged(transaction)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    fieldValue: String,
    icon: ImageVector? = null,
    isNumber: Boolean = false,
    error: String?,
    onfieldInputChanged: (String) -> Unit
) {

    Column {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = onfieldInputChanged,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge,
            isError = error != null,
            keyboardOptions = if (isNumber) KeyboardOptions(
                keyboardType = KeyboardType.Number
            ) else KeyboardOptions.Default,
            trailingIcon = {
                if (icon != null) {
                    Icon(
                        imageVector = icon, contentDescription = label
                    )
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryField(
    selectedCategory: String,
    categories: List<Category>,
    onCategoryChanged: (String) -> Unit
) {


    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {


        OutlinedTextField(
            value = selectedCategory,
            label = {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            onValueChange = {},
            readOnly = true,
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyLarge,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category.name)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name
                        )
                    },
                    onClick = {
                        onCategoryChanged(category.name)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTransaction(date: Long, onSelectedDate: (Long) -> Unit) {
    var showModal by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date
    )


    OutlinedTextField(
        value = date.toHumanDate(),
        onValueChange = {},
        label = {
            Text(
                text = "Date",
                style = MaterialTheme.typography.labelMedium
            )
        },
        readOnly = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange, contentDescription = "Date"
            )
        },
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )


    if (showModal) {
        DatePickerDialog(
            onDismissRequest = { showModal = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onSelectedDate(it)
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