package com.example.lojapdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    // Fetch user's cart on first composition
    LaunchedEffect(Unit) {
        cartViewModel.fetchCurrentUserCart()
    }

    val cart by cartViewModel.cart.collectAsState()
    val carsInCart by cartViewModel.carsInCart.collectAsState()
    val ownerName by cartViewModel.ownerName.collectAsState()
    val totalPrice = remember(carsInCart) { cartViewModel.getTotalPrice() }


    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "🛒 Carrinho Compartilhado", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Display Owner Name
        Text(text = "👤 Dono: $ownerName")

        // Display Shared Users
        Text(text = if (cart?.userIds?.isNotEmpty() == true) {
            "🤝 Compartilhado com: ${cart?.userIds?.joinToString(", ")}"
        } else {
            "🤝 Não compartilhado com ninguém"
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Display Cars
        Text(text = "🚗 Carros no Carrinho:")
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carsInCart) { car ->
                CarItem(car)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Total Price
        Text(
            text = "💵 Total: $${"%.2f".format(totalPrice)}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar")
        }
    }
}

@Composable
fun CarItem(car: com.example.lojapdm.domain.model.Car) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "🚘 ${car.brand} ${car.model}")
            Text(text = "💲 Preço: $${car.price}")
            Text(text = "📅 Ano: ${car.year}")
        }
    }
}
