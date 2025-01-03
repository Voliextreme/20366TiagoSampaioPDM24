package com.example.lojapdm.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lojapdm.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLogin) "Iniciar Sessão" else "Registar",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Palavra-passe") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isLogin) {
                        authViewModel.loginUser(email, password) { success, error ->
                            if (success) {
                                message = "Sessão iniciada com sucesso!"
                                navController.navigate("home")
                            } else {
                                message = error ?: "Erro ao iniciar sessão"
                            }
                        }
                    } else {
                        authViewModel.registerUser(email, password) { success, error ->
                            if (success) {
                                message = "Registo bem-sucedido!"
                                navController.navigate("home")
                            } else {
                                message = error ?: "Erro ao registar"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLogin) "Entrar" else "Registar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { isLogin = !isLogin },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLogin) "Não tem conta? Registe-se aqui" else "Já tem conta? Inicie sessão")
            }

            Spacer(modifier = Modifier.height(8.dp))

            message?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}
