package com.example.lojapdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.data.repository.UserRepository
import com.example.lojapdm.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _registrationResult = MutableStateFlow<Result<Unit>?>(null)
    val registrationResult: StateFlow<Result<Unit>?> = _registrationResult

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val email = userRepository.getCurrentUserEmail()
            val user = userRepository.getUserByEmail(email)
            _currentUser.value = user
        }
    }

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationResult.value = userRepository.registerUser(name, email, password)
        }
    }
}
