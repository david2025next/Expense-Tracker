package com.example.expensetracker.presentation.AddTransaction

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
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun AddTransactionRoute() {}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionScreen(modifier: Modifier = Modifier) {

    Scaffold(
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
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TransactionFilterSelector()
            Spacer(Modifier.height(20.dp))
            InputField(
                label = "Description",
                fieldValue = "",
                icon = Icons.AutoMirrored.Filled.Notes,
            ) {}
            InputField(
                label = "Amount",
                fieldValue = "",
                icon = Icons.Default.AttachMoney,
            ) {}

            CategoryField(
                selectedCategory = expenseCategories.first(),
                categories = expenseCategories
            ) { }

            DateTransaction(
                date = "21 oct, 2025"
            ) { }

            Button(
                onClick = {},
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Ajouter")
            }
        }
    }
}

data class Category(
    val icon: ImageVector,
    val name: String
)

private val expenseCategories = listOf(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTransaction(date: String, onSelectedDate: (Long) -> Unit) {
    var showModal by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Date",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = date,
            onValueChange = {},
            readOnly = true,
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
            maxLines = 1,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryField(
    selectedCategory: Category,
    categories: List<Category>,
    onCategoryChanged: (String) -> Unit
) {


    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Category",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = selectedCategory.name,
                onValueChange = {},
                readOnly = true,
                shape = MaterialTheme.shapes.small,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
        }

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

@Composable
private fun InputField(
    label: String,
    fieldValue: String,
    icon: ImageVector? = null,
    isNumber: Boolean = false,
    onfieldInputChanged: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = fieldValue,
            onValueChange = onfieldInputChanged,
            modifier = Modifier.fillMaxWidth(),
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
            maxLines = 1,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

val PrimaryCyan = Color(0xFF00BCD4)


@Composable
private fun TransactionFilterSelector() {

    var selectedIndex by remember { mutableIntStateOf(0) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(24.dp))
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF5F5F5))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TransactionFilterSelector.entries.forEachIndexed { index, transaction ->
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) PrimaryCyan else Color.Transparent)
                    .clickable {
                        selectedIndex = index
                        // changed category list onTransactionFilterChanged
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.displayName,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

enum class TransactionFilterSelector(val displayName: String) {
    EXPENSE("Depense"),
    INCOME("Revenu")
}