package com.example.lojapdm.data.repository

import com.example.lojapdm.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val usersCollection = db.collection("users")

    suspend fun getUserByEmail(email: String?): User? {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                document.toObject(User::class.java)?.copy(id = document.id)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun registerUser(name: String, email: String, password: String): Result<Unit> {
        val existingUser = getUserByEmail(email)

        if (existingUser != null) {
            // Explicitly define the failure type as Unit
            return Result.failure<Unit>(Exception("Email is already in use"))
        }

        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid

            if (userId != null) {
                val user = User(id = userId, name = name, email = email)
                usersCollection.document(userId).set(user).await()
                return Result.success(Unit)
            }
            // Explicitly define the failure type as Unit
            return Result.failure<Unit>(Exception("Error registering user"))
        } catch (e: Exception) {
            // Explicitly define the failure type as Unit
            return Result.failure<Unit>(e)
        }
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}
