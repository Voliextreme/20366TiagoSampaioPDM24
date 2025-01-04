package com.example.lojapdm.data.repository

import com.example.lojapdm.domain.model.Car
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CarRepository {
    private val db = FirebaseFirestore.getInstance()
    private val carCollection = db.collection("cars")

    suspend fun getAllCars(): List<Car> {
        return try {
            carCollection.get().await().documents.mapNotNull { document ->
                val car = document.toObject(Car::class.java)
                car?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}