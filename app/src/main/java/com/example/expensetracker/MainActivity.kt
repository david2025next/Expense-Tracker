package com.example.expensetracker

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.expensetracker.presentation.addTransaction.AddTransactionRoute
import com.example.expensetracker.presentation.home.HomeRoute
import com.example.expensetracker.presentation.register.RegisterRouter
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
            ExpenseTrackerTheme(darkTheme = true) {
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
        val startDest = if (loginState == LoginState.LoggedIn) "HOME_GRAPH" else "REGISTER_GRAPH"
        NavHost(navHostController, startDestination = startDest) {
            homeGraph(navHostController)
            registerGraph(navHostController)
            formGraph(navHostController)
        }
    }

    private fun NavGraphBuilder.homeGraph(navHostController: NavHostController) {
        navigation(startDestination = "HOME", route = "HOME_GRAPH") {
            composable("HOME") {
                HomeRoute { navHostController.navigate("FORM") }
            }
        }
    }

    private fun NavGraphBuilder.registerGraph(navHostController: NavHostController) {
        navigation(startDestination = "REGISTER", route = "REGISTER_GRAPH") {
            composable("REGISTER") {
                RegisterRouter {
                    navHostController.popBackStack()
                }
            }
        }
    }

    private fun NavGraphBuilder.formGraph(navHostController: NavHostController) {
        navigation(startDestination = "FORM", route = "FORM_GRAPH") {
            composable("FORM") {
                AddTransactionRoute { navHostController.popBackStack() }
            }
        }
    }
}