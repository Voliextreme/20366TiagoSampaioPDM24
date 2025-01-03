package com.example.lojapdm.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.viewmodel.CarViewModel

@Composable
fun HomeScreen(navController: NavController, carViewModel: CarViewModel) {
    val carros by carViewModel.carros.collectAsState()

    // Buscar carros na primeira renderização
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

        LazyColumn {
            items(carros) { carro ->
                CarroCard(
                    carro = carro,
                    onAddToCart = { /* Lógica para adicionar ao carrinho */ },
                    onClick = {
                        carViewModel.fetchCarroDetalhes(carro.id)
                        navController.navigate("carDetail/${carro.id}")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Terminar Sessão")
        }
    }
}

@Composable
fun CarroCard(carro: com.example.lojapdm.data.model.Carro, onAddToCart: () -> Unit, onClick: () -> Unit) {
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
