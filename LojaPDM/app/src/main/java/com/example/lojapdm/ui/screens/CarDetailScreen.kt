package com.example.lojapdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lojapdm.viewmodel.CarViewModel

@Composable
fun CarDetailScreen(
    navController: NavController,
    carId: String,
    carViewModel: CarViewModel = viewModel()
) {
    // Fetch car details when the screen is first displayed
    LaunchedEffect(carId) {
        carViewModel.fetchCarroDetalhes(carId)
    }

    // Get the car details from the ViewModel
    val carro by carViewModel.carroDetalhes.collectAsState()

    // If the car details are not available yet, show a loading indicator
    if (carro == null) {
        CircularProgressIndicator()
    } else {
        // Display car details
        carro?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Marca: ${it.marca}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Modelo: ${it.modelo}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Preço: €${it.preco}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Descrição: ${it.descricao}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Voltar")
                }
            }
        }
    }
}
