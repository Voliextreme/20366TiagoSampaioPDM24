package com.example.lojapdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    cartViewModel: CartViewModel,
    cartId: String? // Pass the cartId to the ViewModel
) {
    // Fetch the cart details when cartId changes
    LaunchedEffect(cartId) {
        if (cartId != null) {
            cartViewModel.fetchCart(cartId) // Fetch cart information based on the cartId
        } else {
            println("Cart ID is null")
        }
    }

    // Collect state from ViewModel
    val cart by cartViewModel.cart.collectAsState()
    val carsInCart by cartViewModel.carsInCart.collectAsState()
    val ownerName by cartViewModel.ownerName.collectAsState()
    val sharedUserNames by cartViewModel.sharedUserNames.collectAsState()
    val isLoading by cartViewModel.isLoading.collectAsState() // Get the loading state

    var emailErrorMessage by remember { mutableStateOf<String?>(null) }

    var emailToAdd by remember { mutableStateOf("") }
    var showEmailField by remember { mutableStateOf(false) }

    val totalPrice = remember(carsInCart) { cartViewModel.getTotalPrice() }

    LaunchedEffect(Unit) {
        cartViewModel.getAllCarts()
    }

    // Column Layout for CartScreen UI
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(text = "🛒 Carrinho Compartilhado", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

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

            // Button to show the email input field
            IconButton(onClick = { showEmailField = !showEmailField }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Adicionar utilizador"
                )
            }
        }

        // Email field and error message for adding a user
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
                                emailToAdd = ""
                                showEmailField = false
                                emailErrorMessage = null
                            } else {
                                emailErrorMessage = "Email não encontrado no sistema. Tente novamente."
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Partilhar o carrinho")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "🚗 Carros no Carrinho:")
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carsInCart) { car ->
                CarItem(
                    car = car,
                    onRemoveClick = {
                        cartViewModel.removeCarFromCart(cart?.id ?: "", car.id)
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

    // Control the visibility of the next/previous navigation arrows
    Box(modifier = Modifier.fillMaxSize()) {
        if (cartViewModel.userCartIds.isNotEmpty() && cartViewModel.currentCartIndex < cartViewModel.userCartIds.size - 1) {
            IconButton(
                onClick = {
                    cartViewModel.navigateToNextCart()
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Cart",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cartViewModel.userCartIds.isNotEmpty() && cartViewModel.currentCartIndex > 0) {
            IconButton(
                onClick = {
                    cartViewModel.navigateToPreviousCart()
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Cart",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
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
