package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.presentation.form.AddExpenseScreen
import com.example.expensetracker.presentation.home.DashboardScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            App(navHostController)
        }
    }
}


@Composable
private fun App(navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = "Home"){

        composable("Home"){
            DashboardScreen{navHostController.navigate("Form")}
        }
        composable("Form"){
            AddExpenseScreen {
                navHostController.popBackStack()
            }
        }
    }
}