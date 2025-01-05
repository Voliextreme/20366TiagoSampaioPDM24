package com.example.lojapdm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojapdm.data.repository.CartRepository
import com.example.lojapdm.domain.model.Car
import com.example.lojapdm.domain.model.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartViewModel(private val repository: CartRepository = CartRepository()) : ViewModel() {

    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart

    private val _carsInCart = MutableStateFlow<List<Car>>(emptyList())
    val carsInCart: StateFlow<List<Car>> = _carsInCart

    private val _ownerName = MutableStateFlow<String>("")
    val ownerName: StateFlow<String> = _ownerName

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

    // Fetch current user's cart
    fun fetchCurrentUserCart() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    val db = FirebaseFirestore.getInstance()
                    val cartSnapshot = db.collection("carts")
                        .whereEqualTo("ownerId", currentUser.uid)
                        .get()
                        .await()

                    if (!cartSnapshot.isEmpty) {
                        val cart = cartSnapshot.documents.first().toObject(Cart::class.java)
                        cart?.let {
                            _cart.value = it.copy(id = cartSnapshot.documents.first().id)
                            fetchOwnerName(it.ownerId)
                            fetchCarsInCart(it.carIds)
                        }
                    } else {
                        println("No cart found for the current user.")
                    }
                } catch (e: Exception) {
                    println("Error fetching user's cart: ${e.message}")
                }
            }
        } else {
            println("User is not authenticated.")
        }
    }

    private suspend fun fetchOwnerName(ownerId: String) {
        val db = FirebaseFirestore.getInstance()
        val userDoc = db.collection("users").document(ownerId).get().await()
        _ownerName.value = userDoc.getString("name") ?: "Unknown Owner"
    }

    private suspend fun fetchCarsInCart(carIds: List<String>) {
        val cars = mutableListOf<Car>()
        for (carId in carIds) {
            val carDoc = FirebaseFirestore.getInstance().collection("cars").document(carId).get().await()
            carDoc.toObject(Car::class.java)?.let { car ->
                cars.add(car.copy(id = carDoc.id))
            }
        }
        _carsInCart.value = cars


    }

    fun getTotalPrice(): Double {
        return _carsInCart.value.sumOf { it.price}
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