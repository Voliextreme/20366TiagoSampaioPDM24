package com.example.lojapdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState: StateFlow<Result<Unit>?> = _loginState

    private val _isLoggedIn = MutableStateFlow(false)  // Initialize as false
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val auth = FirebaseAuth.getInstance()


    init {
        // Check if a user is already logged in when the ViewModel is created
        checkIfLoggedIn()
    }

    // Check if the user is already logged in when the app starts or is restarted
    private fun checkIfLoggedIn() {
        _isLoggedIn.value = auth.currentUser != null
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginState.value = result
            if (result.isSuccess) {
                _isLoggedIn.value = true
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _loginState.value = null  // Reset login state
        _isLoggedIn.value = false
    }

}