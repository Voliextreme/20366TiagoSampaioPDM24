package com.example.lojapdm.data.repository

import android.system.Os.remove
import com.example.lojapdm.domain.model.Car
import com.example.lojapdm.domain.model.Cart
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class CartRepository {
    private val db = FirebaseFirestore.getInstance()
    private val cartCollection = db.collection("carts")


    // Create or fetch the cart for the user
    suspend fun createCart(ownerId: String): String? {
        val cart = Cart(ownerId = ownerId, userIds = emptyList())
        return try {
            // Check if the user already has a cart
            val existingCart = cartCollection.whereEqualTo("ownerId", ownerId).get().await()
            if (!existingCart.isEmpty) {
                // Return the ID of the existing cart if the user already has one
                return existingCart.documents[0].id
            }

            // If no cart exists, create a new one
            val document = cartCollection.add(cart).await()
            val newCartId = document.id
            println("Cart created with ID: $newCartId")  // Log cart creation
            newCartId
        } catch (e: Exception) {
            // Log error if cart creation fails
            println("Error creating cart: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Add car to cart
    suspend fun addCarToCart(cartId: String, carId: String) {
        try {
            val cartDocument = cartCollection.document(cartId).get().await()

            if (!cartDocument.exists()) {
                // If the cart does not exist, create a new one
                println("Cart not found, creating a new one...")
                val newCartId = createCart(cartDocument.getString("ownerId") ?: "")
                if (newCartId != null) {
                    // Add the car to the new cart
                    cartCollection.document(newCartId)
                        .update("carIds", com.google.firebase.firestore.FieldValue.arrayUnion(carId))
                        .await()
                    println("Car added to new cart with ID: $newCartId")
                } else {
                    println("Failed to create a new cart.")
                    throw Exception("Failed to create a new cart.")
                }
            } else {
                // If the cart exists, add the car to the cart
                cartCollection.document(cartId)
                    .update("carIds", com.google.firebase.firestore.FieldValue.arrayUnion(carId))
                    .await()
                println("Car added to existing cart with ID: $cartId")
            }
        } catch (e: Exception) {
            // Log any error encountered during the process
            println("Error adding car to cart: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    // Get cart by ID
    suspend fun getCart(cartId: String): Cart? {
        return try {
            val snapshot = cartCollection.document(cartId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(Cart::class.java)?.copy(id = snapshot.id)
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error getting cart: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun removeCarFromCart(cartId: String, carId: String): Cart? {
        return try {
            val cartDocument = cartCollection.document(cartId).get().await()

            if (!cartDocument.exists()) {
                println("Cart not found.")
                return null
            }

            // Get the current list of carIds from the cart
            val cart = cartDocument.toObject(Cart::class.java)

            if (cart != null) {
                val updatedCarIds = cart.carIds.toMutableList().apply {
                    remove(carId)
                }

                // Update the cart in Firestore with the new list of carIds
                cartCollection.document(cartId)
                    .update("carIds", updatedCarIds)
                    .await()

                println("Car removed from cart successfully.")

                // Return the updated cart
                cart.copy(carIds = updatedCarIds)
            } else {
                println("Failed to parse cart data.")
                null
            }
        } catch (e: Exception) {
            println("Error removing car from cart: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun getCarDetails(carId: String): Car? {
        return try {
            val snapshot = db.collection("cars").document(carId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(Car::class.java)?.copy(id = snapshot.id)
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error fetching car details: ${e.message}")
            null
        }
    }


}