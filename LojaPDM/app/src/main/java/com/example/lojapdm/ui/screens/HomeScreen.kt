package com.example.lojapdm.ui.screens

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    carViewModel: CarViewModel = CarViewModel(),
    authViewModel: AuthViewModel
) {
    // Collect the state of the cars
    val cars by carViewModel.cars.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Loja de Carros") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Display the list of cars
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(cars) { car ->  // Ensure 'cars' is a List<Car>
                    CarCard(car = car)
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
}

@Composable
fun CarCard(car: Car) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Brand: ${car.brand}")
            Text(text = "Model: ${car.model}")
            Text(text = "Price: $${car.price}")
            Text(text = "Year: ${car.year}")
        }
    }
}
