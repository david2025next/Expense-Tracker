package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.presentation.addTransaction.AddTransactionRoute
import com.example.expensetracker.presentation.home.HomeRoute
import com.example.expensetracker.presentation.register.RegisterRouter
import com.example.expensetracker.ui.navigation.Screen
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var  mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.loginState.value == LoginState.LOADING
        }
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme() {
                RootNavHost(mainViewModel)
            }
        }
    }

    @Composable
    private fun RootNavHost(mainViewModel: MainViewModel) {
        val navHostController = rememberNavController()
        val loginState by mainViewModel.loginState.collectAsStateWithLifecycle()

        if(loginState== LoginState.LOADING){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
            return
        }
        val startDest = if (loginState == LoginState.LoggedIn) Screen.Home.route else Screen.Register.route
        NavHost(navHostController, startDestination = startDest) {
            homeGraph(navHostController)
            registerGraph(navHostController)
            formGraph(navHostController)
        }
    }

    private fun NavGraphBuilder.homeGraph(navHostController: NavHostController) {
        composable(Screen.Home.route) {
            HomeRoute { navHostController.navigate(Screen.Form.route) }
        }
    }

    private fun NavGraphBuilder.registerGraph(navHostController: NavHostController) {
        composable(Screen.Register.route) {
            RegisterRouter {
                navHostController.popBackStack()
            }
        }
    }

    private fun NavGraphBuilder.formGraph(navHostController: NavHostController) {
        composable(Screen.Form.route) {
            AddTransactionRoute { navHostController.popBackStack() }
        }
    }
}