package com.example.lojapdm.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.domain.model.Car
import com.example.lojapdm.viewmodel.CarViewModel
import com.example.lojapdm.viewmodel.AuthViewModel
import com.example.lojapdm.viewmodel.CartViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    carViewModel: CarViewModel = CarViewModel(),
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel = remember { CartViewModel() }
) {
    val cars by carViewModel.cars.collectAsState() // List of cars from the database
    val cart by cartViewModel.cart.collectAsState() // Observe the cart from CartViewModel
    val cartItemCount = cart?.carIds?.size ?: 0 // Get the number of cars in the cart
    val ownerId = FirebaseAuth.getInstance().currentUser?.uid

    // Fetch or create the user's cart when HomeScreen loads
    LaunchedEffect(ownerId) {
        ownerId?.let {
            cartViewModel.createOrFetchCart()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Loja de Carros") },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text(cartItemCount.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = {
                            cartViewModel.cart.value?.id?.let { cartId ->
                                navController.navigate("cart/$cartId")
                            } ?: Log.e("Navigation", "Cart ID is null")
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.ShoppingCart,
                                contentDescription = "Shopping cart"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(cars) { car ->
                    val isInCart = cart?.carIds?.contains(car.id) == true
                    CarCard(
                        car = car,
                        onAddToCart = { carId ->
                            cart?.id?.let { cartId ->
                                cartViewModel.addCarToCart(cartId, carId)
                            } ?: Log.e("Cart", "Cart not found for the user.")
                        },
                        isInCart = isInCart // Pass the flag
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to log out
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true } // Clear entire back stack
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text("Terminar Sessão")
            }
        }
    }
}

@Composable
fun CarCard(car: Car, onAddToCart: (String) -> Unit, isInCart: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
    colors = CardDefaults.cardColors(
            containerColor = if (isInCart) Color.LightGray else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Marca: ${car.brand}")
                Text(text = "Modelo: ${car.model}")
                Text(text = "Preço: $${car.price}")
                Text(text = "Ano: ${car.year}")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { onAddToCart(car.id) },
                enabled = !isInCart, // Disable if the car is already in the cart
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = if (isInCart) "Already in cart" else "Add to cart",
                    tint = if (isInCart) Color.Gray else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}