package com.example.lojapdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lojapdm.navigation.AppNavigation
import com.example.lojapdm.ui.theme.LojaPDMTheme
import com.example.lojapdm.viewmodel.AuthViewModel
import com.example.lojapdm.viewmodel.CarViewModel
import com.example.lojapdm.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaPDMTheme {
                // Criar os ViewModels
                val authViewModel: AuthViewModel = viewModel()
                val carViewModel: CarViewModel = viewModel()
                val userViewModel: UserViewModel = viewModel()

                // Chamar a Navegação
                AppNavigation(
                    authViewModel = authViewModel,
                    carViewModel = carViewModel,
                    userViewModel = userViewModel
                )
            }
        }
    }
}
