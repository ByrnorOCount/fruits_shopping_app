package com.mopr.fruits_app.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mopr.fruits_app.models.Fruit
import com.mopr.fruits_app.models.User
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val tag = "FirestoreManager"
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val usersCollection = db.collection("users")
    private val fruitsCollection = db.collection("fruits")

    suspend fun registerUser(user: User): Boolean {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val uid = authResult.user?.uid ?: return false
            val userWithId = user.copy(id = uid)
            usersCollection.document(uid).set(userWithId).await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error registering user: ${e.message}", e)
            false
        }
    }

    suspend fun loginUser(identifier: String, password: String): User? {
        return try {
            val emailToUse = if (identifier.contains("@")) {
                identifier
            } else {
                // Try to find email by username
                val query = usersCollection.whereEqualTo("username", identifier).limit(1).get().await()
                if (query.isEmpty) {
                    Log.e(tag, "No user found with username: $identifier")
                    return null
                }
                query.documents[0].getString("email") ?: return null
            }

            val authResult = auth.signInWithEmailAndPassword(emailToUse, password).await()
            val uid = authResult.user?.uid ?: return null
            val document = usersCollection.document(uid).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e(tag, "Error logging in: ${e.message}", e)
            null
        }
    }

    suspend fun getAllFruits(): List<Fruit> {
        return try {
            val snapshot = fruitsCollection.get().await()
            snapshot.toObjects(Fruit::class.java)
        } catch (e: Exception) {
            Log.e(tag, "Error getting all fruits: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun seedFruits(fruits: List<Fruit>): Boolean {
        return try {
            val snapshot = fruitsCollection.limit(1).get().await()
            if (snapshot.isEmpty) {
                for (fruit in fruits) {
                    fruitsCollection.add(fruit).await()
                }
                Log.d(tag, "Fruits seeded successfully")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(tag, "Error seeding fruits: ${e.message}", e)
            false
        }
    }

    suspend fun seedUsers(users: List<User>) {
        for (user in users) {
            try {
                val query = usersCollection.whereEqualTo("username", user.username).limit(1).get().await()
                if (query.isEmpty) {
                    registerUser(user)
                    Log.d(tag, "Seeded new user: ${user.username}")
                } else {
                    // Force update the isAdmin field to ensure it's correct in the DB
                    val uid = query.documents[0].id
                    usersCollection.document(uid).update("isAdmin", user.isAdmin).await()
                    Log.d(tag, "Updated existing user perms: ${user.username}")
                }
            } catch (e: Exception) {
                Log.e(tag, "Error seeding user ${user.username}: ${e.message}")
            }
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}
