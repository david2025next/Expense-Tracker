package com.example.expensetracker.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


data class Category(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

val categoriesMenu = listOf(
    Category(
        "Transport",
        Icons.Default.DirectionsCar,
        Color.Yellow
    ),
    Category(
        "Alimentation",
        Icons.Default.Restaurant,
        Color.Blue
    ),
)