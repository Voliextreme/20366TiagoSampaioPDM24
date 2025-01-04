package com.example.lojapdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lojapdm.viewmodel.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = CartViewModel()
) {
    val cart by cartViewModel.cart.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Carrinho Compartilhado", style = MaterialTheme.typography.titleLarge)

        cart?.let {
            Text(text = "Dono: ${it.ownerId}")
            Text(text = "Compartilhado com: ${it.userIds.joinToString(", ")}")
            Text(text = "Carros no Carrinho:")

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(it.carIds) { carId ->
                    Text(text = "Car ID: $carId")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}
