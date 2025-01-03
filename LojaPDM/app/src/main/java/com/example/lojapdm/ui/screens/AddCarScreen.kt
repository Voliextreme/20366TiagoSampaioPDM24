package com.example.lojapdm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.viewmodel.CarViewModel

@Composable
fun AddCarScreen(
    navController: NavController,
    carViewModel: CarViewModel
) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Adicionar Carro",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = marca,
            onValueChange = { marca = it },
            label = { Text("Marca") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = modelo,
            onValueChange = { modelo = it },
            label = { Text("Modelo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth()
        )

        //descricao
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Add the car to the database
                val precoDouble = preco.toDoubleOrNull()

                // Check if fields are not blank and preco is a valid number
                if (marca.isNotBlank() && modelo.isNotBlank() && precoDouble != null) {
                    // Proceed to add the car if all fields are valid
                    carViewModel.addCarro(marca, modelo, precoDouble, descricao,
                        onSuccess = {
                            // Success case, navigate back to the home screen
                            navController.popBackStack()
                        },
                        onFailure = { errorMessage ->
                            // Handle failure case, show an error message
                            Toast.makeText(navController.context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    // Handle the error case when preco is invalid or any field is blank
                    Toast.makeText(navController.context, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Carro")
        }
    }
}
