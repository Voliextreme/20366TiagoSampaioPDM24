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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var userCartIds: List<String> = emptyList()

    var currentCartIndex = 0
    private val cartNavigationStack = mutableListOf<String>()



    fun createOrFetchCart() {
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid
        if (ownerId != null) {
            viewModelScope.launch {
                try {
                    val cartId = repository.createCart(ownerId)
                    if (cartId != null) {
                        fetchCart(cartId)
                    }
                } catch (e: Exception) {
                    println("Error in createOrFetchCart: ${e.message}")
                }
            }
        } else {
            println("User is not authenticated.")
        }
    }

    fun addCarToCart(cartId: String, carId: String) {
        viewModelScope.launch {
            try {
                repository.addCarToCart(cartId, carId)
                fetchCart(cartId)
            } catch (e: Exception) {
                println("Error adding car to cart: ${e.message}")
            }
        }
    }

    fun fetchCart(cartId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedCart = repository.getCart(cartId)
                _cart.value = fetchedCart

                // Fetch related data after fetching the cart
                fetchedCart?.let {
                    fetchOwnerName(it.ownerId)
                    fetchCarsInCart(it.carIds)
                    fetchSharedUserNames(it.userIds)
                }
            } catch (e: Exception) {
                println("Error fetching cart: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
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
                    onResult(false)
                } else {
                    val userDoc = userQuery.documents.first()
                    val userId = userDoc.id
                    val cartDocRef = db.collection("carts").document(cartId)
                    val cartSnapshot = cartDocRef.get().await()

                    if (cartSnapshot.exists()) {
                        val cart = cartSnapshot.toObject(Cart::class.java)
                        cart?.let {
                            val updatedUserIds = it.userIds.toMutableList().apply { add(userId) }
                            cartDocRef.update("userIds", updatedUserIds).await()
                            fetchSharedUserNames(updatedUserIds)
                            onResult(true)
                        }
                    }
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun getTotalPrice(): Double {
        return _carsInCart.value.sumOf { it.price }
    }

    fun removeCarFromCart(cartId: String, carId: String) {
        viewModelScope.launch {
            try {
                val updatedCart = repository.removeCarFromCart(cartId, carId)
                if (updatedCart != null) {
                    _cart.value = updatedCart
                    fetchCarsInCart(updatedCart.carIds)
                }
            } catch (e: Exception) {
                println("Error removing car from cart: ${e.message}")
            }
        }
    }

    fun getAllCarts() {
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid
        if (ownerId != null) {
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    val db = FirebaseFirestore.getInstance()
                    val cartsQuery = db.collection("carts").get().await()

                    val carts = cartsQuery.documents.mapNotNull { doc ->
                        val cart = doc.toObject(Cart::class.java)
                        cart?.apply {
                            this.id = doc.id // Explicitly set the document ID into the Cart object
                        }

                        // Include all carts where the user is in userIds
                        cart?.takeIf { it.userIds.contains(ownerId) || it.ownerId == ownerId}
                    }

                    val ownerCart = carts.firstOrNull { it.ownerId == ownerId }
                    val otherCarts = carts.filterNot { it.ownerId == ownerId }


                    userCartIds = buildList {
                        ownerCart?.let { add(it.id) }
                        addAll(otherCarts.map { it.id })
                    }


                    if (userCartIds.isNotEmpty()) {
                        fetchCart(userCartIds[0])
                    }

                } catch (e: Exception) {
                    println("Error fetching all carts: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            println("User is not authenticated.")
        }
    }


    fun navigateToNextCart() {
        Log.e("userCartIds", "$userCartIds")
        if (userCartIds.isNotEmpty() && currentCartIndex < userCartIds.size - 1) {
            currentCartIndex++
            val nextCartId = userCartIds[currentCartIndex]
            fetchCart(nextCartId)
        }
    }

    // Function to navigate to the previous cart
    fun navigateToPreviousCart() {
        if (userCartIds.isNotEmpty() && currentCartIndex > 0) {
            currentCartIndex--
            val previousCartId = userCartIds[currentCartIndex]
            fetchCart(previousCartId)
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
            carDoc.toObject(Car::class.java)?.let { car -> cars.add(car.copy(id = carDoc.id)) }
        }
        _carsInCart.value = cars
    }

    private suspend fun fetchSharedUserNames(userIds: List<String>) {
        val db = FirebaseFirestore.getInstance()
        val sharedUserNames = mutableListOf<String>()
        try {
            userIds.forEach { userId ->
                val userDoc = db.collection("users").document(userId).get().await()
                val userName = userDoc.getString("name") ?: "Unknown User"
                sharedUserNames.add(userName)
            }
            _sharedUserNames.value = sharedUserNames
        } catch (e: Exception) {
            println("Error fetching shared user names: ${e.message}")
        }
    }
}
