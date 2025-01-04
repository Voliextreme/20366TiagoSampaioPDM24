package com.example.lojapdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lojapdm.ui.screen.AuthScreen
import com.example.lojapdm.ui.screens.*
import com.example.lojapdm.viewmodel.AuthViewModel
import com.example.lojapdm.viewmodel.CarViewModel
import com.example.lojapdm.viewmodel.UserViewModel

// Define as rotas
sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")

}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    carViewModel: CarViewModel,
    userViewModel: UserViewModel
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(navController = navController, userViewModel, authViewModel = authViewModel)
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                carViewModel = carViewModel,
                authViewModel = authViewModel
            )
        }
    }
}
