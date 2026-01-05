package com.example.expensetracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.utils.categoriesMenu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryMenu() {

    var expanded by remember { mutableStateOf(false) }
    var categorySelected by remember { mutableStateOf(categoriesMenu.first()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = categorySelected.name,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)}
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoriesMenu.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.name,
                                tint = category.color
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = category.name
                            )
                        }
                    },
                    onClick = {
                        categorySelected = category
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
    isNumber: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(

    ) {
        Text("$label ${if (isNumber) "" else " (optional)"}")
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label
                )
            },
            singleLine = if (isNumber) true else false,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isNumber) KeyboardType.Number else KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth(),
            suffix = {
                if (isNumber) Text("FCFA")
                else null
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InputFieldNumberPreview() {
    var amount by remember { mutableIntStateOf(0) }
    InputField(
        value = amount.toString(),
        isNumber = true,
        label = "Amount",
        icon = Icons.Default.AttachMoney
    ) { amount = it.toInt() }
}

@Preview(showBackground = true)
@Composable
private fun InputFieldDescriptionPreview() {
    var description by remember { mutableStateOf("") }
    InputField(
        value = description,
        isNumber = false,
        label = "Description",
        icon = Icons.AutoMirrored.Filled.Notes
    ) { description = it }
}


@Preview(showBackground = true)
@Composable
private fun CategoryMenuPreview(){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        CategoryMenu()
    }
}


