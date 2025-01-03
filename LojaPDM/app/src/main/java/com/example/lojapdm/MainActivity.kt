package com.example.lojapdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lojapdm.navigation.AppNavigation
import com.example.lojapdm.ui.screens.AuthScreen
import com.example.lojapdm.ui.screens.HomeScreen
import com.example.lojapdm.ui.theme.LojaPDMTheme
import com.example.lojapdm.viewmodel.AuthViewModel
import com.example.lojapdm.viewmodel.CarViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaPDMTheme {
                // Criar os ViewModels
                val authViewModel: AuthViewModel = viewModel()
                val carViewModel: CarViewModel = viewModel()

                // Chamar a Navegação
                AppNavigation(
                    authViewModel = authViewModel,
                    carViewModel = carViewModel
                )
            }
        }
    }
}
