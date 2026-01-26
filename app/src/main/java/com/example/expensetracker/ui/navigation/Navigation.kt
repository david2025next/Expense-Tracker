package com.example.expensetracker.ui.navigation


sealed class Screen(val route : String){
    object Home : Screen("Home")
    object Form : Screen("Form")
    object Register : Screen("Register")
}