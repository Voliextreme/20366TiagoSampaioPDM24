package com.example.lojapdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val sharedUserNames by cartViewModel.sharedUserNames.collectAsState() // Get the shared user names
    var emailErrorMessage by remember { mutableStateOf<String?>(null) }


    // State for email input
    var emailToAdd by remember { mutableStateOf("") }
    var showEmailField by remember { mutableStateOf(false) } // To toggle the email input field

    // Calculate total price whenever the list of cars in the cart changes
    val totalPrice = remember(carsInCart) { cartViewModel.getTotalPrice() }

    // Display UI content
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {        Text(text = "🛒 Carrinho Compartilhado", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Owner and Shared Users
        Text(text = "👤 Dono: $ownerName")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (sharedUserNames.isNotEmpty()) {
                Text(text = "🤝 Compartilhado com: ${sharedUserNames.joinToString(", ")}")
            } else {
                Text(text = "🤝 Não compartilhado com ninguém")
            }

            IconButton(onClick = { showEmailField = !showEmailField }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Adicionar utilizador"
                )
            }
        }

        // Email Input
        if (showEmailField) {
            OutlinedTextField(
                value = emailToAdd,
                onValueChange = { emailToAdd = it },
                label = { Text("Email do utilizador") },
                isError = emailErrorMessage != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailErrorMessage != null) {
                Text(
                    text = emailErrorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    cart?.id?.let {
                        cartViewModel.addUserToCartByEmail(it, emailToAdd) { success ->
                            if (success) {
                                emailToAdd = "" // Reset email input field
                                showEmailField = false // Hide the email input after submission
                                emailErrorMessage = null
                            } else {
                                emailErrorMessage =
                                    "Email não encontrado no sistema. Tente novamente."
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar ao carrinho")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cars in Cart
        Text(text = "🚗 Carros no Carrinho:")
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carsInCart) { car ->
                CarItem(
                    car = car,
                    onRemoveClick = {
                        cartViewModel.removeCarFromCart(car.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Your other content goes here

        // The IconButton positioned to the right-center
        IconButton(
            onClick = { navController.navigate("NextScreen") },
            modifier = Modifier
                .align(Alignment.CenterEnd) // Positioned at center-right
                .padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Próxima Tela",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun CarItem(
    car: com.example.lojapdm.domain.model.Car,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "🚘 ${car.brand} ${car.model}")
                Text(text = "💲 Preço: $${car.price}")
                Text(text = "📅 Ano: ${car.year}")
            }
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remover Carro",
                    tint = Color.Red
                )
            }
        }
    }
}
