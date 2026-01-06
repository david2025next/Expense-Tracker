package com.example.expensetracker.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val name : String,
    val icon : ImageVector
)


val categories = listOf(
    Category("Courses", Icons.Filled.ShoppingCart),
    Category("Alimentation", Icons.Filled.Restaurant),
    Category("Internet", Icons.Filled.Wifi),
    Category("Transport", Icons.Filled.DirectionsCar),
    Category("Divertissement", Icons.Filled.Movie),
    Category("Factures", Icons.Filled.Receipt)
)

// c'est une methode dangereuse aue je dois reparer
fun findIconByCategoryName(name : String) : ImageVector = categories.find{it.name == name}?.icon!!
