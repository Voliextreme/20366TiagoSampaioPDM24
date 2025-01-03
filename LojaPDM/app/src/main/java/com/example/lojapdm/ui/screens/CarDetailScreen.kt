package com.example.lojapdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.viewmodel.CarViewModel

@Composable
fun CarDetailScreen(navController: NavController, carroId: String, carViewModel: CarViewModel) {
    val carro by carViewModel.carroSelecionado.collectAsState()

    LaunchedEffect(carroId) {
        carViewModel.fetchCarroDetalhes(carroId)
    }

    carro?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "${it.marca} - ${it.modelo}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Preço: €${it.preco}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Descrição: ${it.descricao}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Voltar")
            }
        }
    } ?: CircularProgressIndicator()
}
