package com.example.lojapdm.data.repository


import com.example.lojapdm.domain.model.Cart
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartRepository {
    private val db = FirebaseFirestore.getInstance()
    private val cartCollection = db.collection("carts")

    suspend fun getCart(cartId: String): Cart? {
        return try {
            val snapshot = cartCollection.document(cartId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(Cart::class.java)?.copy(id = snapshot.id)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createCart(ownerId: String): String? {
        val cart = Cart(ownerId = ownerId, userIds = emptyList())
        return try {
            val existingCart = cartCollection.whereEqualTo("ownerId", ownerId).get().await()
            if (!existingCart.isEmpty) {
                return existingCart.documents[0].id
            }

            val document = cartCollection.add(cart).await()
            val newCartId = document.id
            println("Cart created with ID: $newCartId")  // Log cart creation
            newCartId
        } catch (e: Exception) {
            println("Error creating cart: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun addCarToCart(cartId: String, carId: String) {
        try {
            val cartDocument = cartCollection.document(cartId).get().await()
            if (cartDocument.exists()) {
                val cart = cartDocument.toObject(Cart::class.java)
                val updatedCarIds = cart?.carIds?.toMutableList()?.apply { add(carId) }
                cartCollection.document(cartId).update("carIds", updatedCarIds).await()
            }
        } catch (e: Exception) {
            println("Error adding car to cart: ${e.message}")
        }
    }

    suspend fun removeCarFromCart(cartId: String, carId: String): Cart? {
        try {
            val cartDocument = cartCollection.document(cartId).get().await()
            if (cartDocument.exists()) {
                val cart = cartDocument.toObject(Cart::class.java)
                val updatedCarIds = cart?.carIds?.toMutableList()?.apply { remove(carId) }
                cartCollection.document(cartId).update("carIds", updatedCarIds).await()
                return cart?.copy(carIds = updatedCarIds ?: emptyList())
            }
        } catch (e: Exception) {
            println("Error removing car from cart: ${e.message}")
        }
        return null
    }
}
