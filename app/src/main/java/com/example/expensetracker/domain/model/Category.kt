package com.example.expensetracker.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val icon: ImageVector,
    val name: String
)

val expenseCategories = listOf(
    Category(
        icon = Icons.Default.Restaurant,
        name = "Alimentation"
    ),
    Category(
        icon = Icons.Default.Wifi,
        name = "Internet"
    )
)

val incomeCategories = listOf(
    Category(
        icon = Icons.Default.Movie,
        name = "Movie"
    ),
    Category(
        icon = Icons.Default.Work,
        name = "Work"
    )
)

fun findIconByCategoryName(name : String) = (expenseCategories+incomeCategories).first { it.name==name }.icon