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
import com.example.lojapdm.viewmodel.CartViewModel
import com.example.lojapdm.viewmodel.UserViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    carViewModel: CarViewModel,
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                navController = navController,
                userViewModel = userViewModel,
                authViewModel = authViewModel
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                carViewModel = carViewModel,
                authViewModel = authViewModel,
                cartViewModel = cartViewModel
            )
        }
        composable("cart/{cartId}") { backStackEntry ->
            val cartId = backStackEntry.arguments?.getString("cartId")
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                cartId = cartId
            )
        }
    }
}
