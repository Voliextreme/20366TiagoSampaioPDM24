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

    private val _sharedUserNames = MutableStateFlow<List<String>>(emptyList())
    val sharedUserNames: StateFlow<List<String>> = _sharedUserNames


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
                            fetchSharedUserNames(it.userIds)
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

    fun addUserToCartByEmail(cartId: String, email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val userQuery = db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                if (userQuery.isEmpty) {
                    println("No user found with email: $email")
                    onResult(false) // Trigger failure callback
                } else {
                    val userDoc = userQuery.documents.first()
                    val userId = userDoc.id

                    val cartDocRef = db.collection("carts").document(cartId)
                    val cartSnapshot = cartDocRef.get().await()

                    if (cartSnapshot.exists()) {
                        val cart = cartSnapshot.toObject(Cart::class.java)
                        cart?.let {
                            val updatedUserIds = it.userIds.toMutableList().apply {
                                add(userId)
                            }
                            cartDocRef.update("userIds", updatedUserIds).await()
                            fetchSharedUserNames(updatedUserIds)
                            onResult(true) // Trigger success callback
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error adding user to cart: ${e.message}")
                onResult(false) // Trigger failure callback
            }
        }
    }



    fun getTotalPrice(): Double {
        return _carsInCart.value.sumOf { it.price}
    }

    fun fetchSharedUserNames(userIds: List<String>) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            val sharedUserNames = mutableListOf<String>()

            try {
                userIds.forEach { userId ->
                    val userDoc = db.collection("users").document(userId).get().await()
                    val userName = userDoc.getString("name") ?: "Unknown User"
                    Log.e("User", userName)
                    sharedUserNames.add(userName)
                }
                _sharedUserNames.value = sharedUserNames
                println("Shared user names updated: $sharedUserNames")
            } catch (e: Exception) {
                println("Error fetching shared user names: ${e.message}")
            }
        }
    }

    fun removeCarFromCart(carId: String) {
        val cartId = _cart.value?.id
        if (cartId != null) {
            viewModelScope.launch {
                try {
                    val updatedCart = repository.removeCarFromCart(cartId, carId)
                    if (updatedCart != null) {
                        _cart.value = updatedCart // Update the cart state

                        // Fetch updated car details
                        val carDetails = updatedCart.carIds.mapNotNull { carId ->
                            repository.getCarDetails(carId)  // Fetch each car's details
                        }

                        _carsInCart.value = carDetails // Update with List<Car>
                    } else {
                        println("Failed to refresh the cart after removing the car.")
                    }
                } catch (e: Exception) {
                    println("Error removing car from cart: ${e.message}")
                }
            }
        } else {
            println("No cart found to remove the car.")
        }
    }

}