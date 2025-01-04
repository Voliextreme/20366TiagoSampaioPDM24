package com.example.lojapdm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.data.repository.CartRepository
import com.example.lojapdm.domain.model.Cart
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class CartViewModel(private val repository: CartRepository = CartRepository()) : ViewModel() {

    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart

    // Create or fetch the user's cart
    fun createOrFetchCart() {
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid
        if (ownerId != null) {
            viewModelScope.launch {
                try {
                    val cartId = repository.createCart(ownerId)  // Ensure cart is created if not already present
                    fetchCart(cartId)
                } catch (e: Exception) {
                    println("Error in createOrFetchCart: ${e.message}")
                }
            }
        } else {
            println("User is not authenticated.")
        }
    }

    // Fetch cart by ID
    fun fetchCart(cartId: String?) {
        if (cartId != null) {
            viewModelScope.launch {
                try {
                    val fetchedCart = repository.getCart(cartId)
                    _cart.value = fetchedCart
                    println("Cart fetched: $fetchedCart")
                } catch (e: Exception) {
                    println("Error fetching cart: ${e.message}")
                }
            }
        }
    }

    // Add car to cart
    fun addCarToCart(carId: String) {
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid
        val cartId = _cart.value?.id

        if (ownerId != null && cartId != null) {
            viewModelScope.launch {
                try {
                    repository.addCarToCart(cartId, carId)
                    Log.e("Car", carId)
                    fetchCart(cartId)
                } catch (e: Exception) {
                    println("Error adding car to cart: ${e.message}")
                }
            }
        } else {
            println("User is not authenticated or cart does not exist.")
        }
    }
/*
    // Remove car from cart
    fun removeCarFromCart(carId: String) {
        val cartId = _cart.value?.id
        if (cartId != null) {
            viewModelScope.launch {
                try {
                    repository.removeCarFromCart(cartId, carId)
                    fetchCart(cartId)
                } catch (e: Exception) {
                    println("Error removing car from cart: ${e.message}")
                }
            }
        } else {
            println("No cart found to remove the car.")
        }
    }

    // Share the cart with another user
    fun shareCartWithUser(userId: String) {
        val cartId = _cart.value?.id
        if (cartId != null) {
            viewModelScope.launch {
                try {
                    repository.shareCartWithUser(cartId, userId)
                    fetchCart(cartId)
                } catch (e: Exception) {
                    println("Error sharing cart: ${e.message}")
                }
            }
        } else {
            println("No cart found to share.")
        }
    }*/
}