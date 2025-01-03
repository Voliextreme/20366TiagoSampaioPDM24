package com.example.lojapdm.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.domain.model.Carro
import com.example.lojapdm.viewmodel.CarViewModel
import com.example.lojapdm.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    carViewModel: CarViewModel,
    authViewModel: AuthViewModel
) {
    val carros by carViewModel.carros.collectAsState()
    //val currentUserRole by authViewModel.currentUserRole.collectAsState() // Observe the role of the current user


    // Fetch cars on first render
    LaunchedEffect(Unit) {
        carViewModel.fetchCarros()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Loja de Carros",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Show the "+" button if the user is an admin
        /*if (currentUserRole == "admin") {
            Button(
                onClick = { navController.navigate("addCar") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }*/



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Logout the user
                authViewModel.logout()

                // Navigate to the auth screen and clear back stack
                navController.navigate("auth") {
                    // Pop the back stack to home and include it, ensuring it's removed
                    popUpTo("home") { inclusive = true }

                    // Ensure the auth screen doesn't get duplicated in the back stack
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Terminar Sessão")
        }
    }
}

@Composable
fun CarroCard(carro: Carro, onAddToCart: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${carro.marca} - ${carro.modelo}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Preço: €${carro.preco}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onAddToCart) {
                Text("+ Adicionar ao Carrinho")
            }
        }
    }
}
