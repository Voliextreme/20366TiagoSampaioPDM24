package com.example.lojapdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.data.repository.AuthRepository
import com.example.lojapdm.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState: StateFlow<Result<Unit>?> = _loginState

    private val _isLoggedIn = MutableStateFlow(authRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val auth = FirebaseAuth.getInstance()

    val userId: String?
        get() = auth.currentUser?.uid

    init {
        // Check if a user is already logged in when the ViewModel is created
        checkIfLoggedIn()
    }

    // Check if the user is already logged in when the app starts or is restarted
    private fun checkIfLoggedIn() {
        _isLoggedIn.value = authRepository.isLoggedIn()
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
        _isLoggedIn.value = false
    }

    fun resetLoginState() {
        _loginState.value = null
    }
}
