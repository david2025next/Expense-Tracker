package com.example.expensetracker.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class CategoryMenu(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

val categoriesMenu = listOf(
    CategoryMenu(
        "Transport",
        Icons.Default.DirectionsCar,
        Color.Yellow
    ),
    CategoryMenu(
        "Alimentation",
        Icons.Default.Restaurant,
        Color.Blue
    ),
)

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}