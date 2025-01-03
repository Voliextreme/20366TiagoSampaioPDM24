package com.example.lojapdm.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojapdm.viewmodel.AuthViewModel
import com.example.lojapdm.viewmodel.UserViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel
) {
    // Observe login state
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // If the user is already logged in, navigate to the home screen
    if (isLoggedIn) {
        LaunchedEffect(Unit) {
            navController.navigate("home") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) } // Toggle between login and register

    val registrationResult by userViewModel.registrationResult.collectAsState()
    val loginState by authViewModel.loginState.collectAsState()

    val context = LocalContext.current // Get the context for Toast

    // Monitor login state changes
    LaunchedEffect(loginState) {
        loginState?.let {
            if (it.isSuccess) {
                showToast(context, "Login Successful")
                navController.navigate("home") {
                    popUpTo("auth") { inclusive = true }
                }
            } else {
                showToast(context, "Login Failed: ${it.exceptionOrNull()?.message}")
            }
        }
    }

    // Monitor registration result
    LaunchedEffect(registrationResult) {
        registrationResult?.let {
            if (it.isSuccess) {
                showToast(context, "Registration Successful")
                navController.navigate("home") {
                    popUpTo("auth") { inclusive = true }
                }
            } else {
                showToast(context, "Registration Failed: ${it.exceptionOrNull()?.message}")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Register view - name field only shown in registration
        if (!isLogin) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        // Common fields
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (isLogin) {
                    authViewModel.login(email, password) // Login
                } else {
                    userViewModel.registerUser(name, email, password) // Register
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLogin) "Login" else "Register")
        }

        // Toggle between login and register
        TextButton(
            onClick = { isLogin = !isLogin }
        ) {
            Text(if (isLogin) "Don't have an account? Register" else "Already have an account? Login")
        }
    }
}

// Function to show Toast messages
fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
