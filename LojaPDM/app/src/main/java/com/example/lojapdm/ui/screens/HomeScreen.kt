package com.example.lojapdm.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.domain.model.Car
import com.example.lojapdm.viewmodel.CarViewModel
import com.example.lojapdm.viewmodel.AuthViewModel
import com.example.lojapdm.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    carViewModel: CarViewModel = CarViewModel(),
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel = remember { CartViewModel() } // Initialize CartViewModel here
) {
    val cars by carViewModel.cars.collectAsState() // List of cars from the database
    val cart = cartViewModel.cart.collectAsState().value // Observe the cart from CartViewModel

    // Fetch or create the user's cart when the HomeScreen is first loaded
    LaunchedEffect(authViewModel.userId) {
        val userId = authViewModel.userId // Get the authenticated user's ID
        if (userId != null) {
            // Check if the user already has a cart, if not, create one
            cartViewModel.createOrFetchCart()
        }
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

        Spacer(modifier = Modifier.height(8.dp))

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Car List") }
                )
            }
        ) { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                items(cars) { car ->
                    CarCard(car = car, onAddToCart = { carId ->
                        Log.e("CarHome", carId) // Debugging
                        if (cart != null) {
                            cartViewModel.addCarToCart(carId)
                        } else {
                            println("Cart not found for the user.")
                        }
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to log out
        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
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
fun CarCard(car: Car, onAddToCart: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "ID: ${car.id}")
                Text(text = "Brand: ${car.brand}")
                Text(text = "Model: ${car.model}")
                Text(text = "Price: $${car.price}")
                Text(text = "Year: ${car.year}")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onAddToCart(car.id) }, // Call the function on click
                modifier = Modifier.size(40.dp)
            ) {
                Text("+")
            }
        }
    }
}