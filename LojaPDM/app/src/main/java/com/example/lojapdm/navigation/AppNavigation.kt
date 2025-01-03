package com.example.lojapdm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lojapdm.ui.screens.*
import com.example.lojapdm.viewmodel.AuthViewModel
import com.example.lojapdm.viewmodel.CarViewModel

// Define as rotas
sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object CarDetail : Screen("carDetail/{carroId}") {
        fun createRoute(carroId: String) = "carDetail/$carroId"
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    carViewModel: CarViewModel
) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (authViewModel.currentUser != null) Screen.Home.route else Screen.Auth.route
    ) {
        // Tela de Autenticação
        composable(Screen.Auth.route) {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Tela Principal
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                carViewModel = carViewModel
            )
        }

        // Tela de Detalhes do Carro
        composable(Screen.CarDetail.route) { backStackEntry ->
            val carroId = backStackEntry.arguments?.getString("carroId") ?: ""
            CarDetailScreen(
                navController = navController,
                carroId = carroId,
                carViewModel = carViewModel
            )
        }
    }
}
