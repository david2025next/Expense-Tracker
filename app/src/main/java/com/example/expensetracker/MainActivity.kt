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
import com.example.expensetracker.presentation.addTransaction.AddTransactionRoute
import com.example.expensetracker.presentation.home.HomeRoute
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ExpenseTrackerTheme{
                val navHostController = rememberNavController()
                ExpenseTracker(navHostController)
            }
        }
    }
}


@Composable
private fun ExpenseTracker(navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = "HOME"){

        composable("HOME"){
            HomeRoute { navHostController.navigate("FORM") }
        }
        composable("FORM"){
            AddTransactionRoute { navHostController.popBackStack() }
        }
    }
}