package com.mopr.fruits_app.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mopr.fruits_app.models.CartItem
import com.mopr.fruits_app.models.Fruit
import com.mopr.fruits_app.models.Order
import com.mopr.fruits_app.models.User
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val tag = "FirestoreManager"
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val usersCollection = db.collection("users")
    private val fruitsCollection = db.collection("fruits")
    private val ordersCollection = db.collection("orders")

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

    suspend fun getUserData(userId: String): User? {
        return try {
            val document = usersCollection.document(userId).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e(tag, "Error getting user data: ${e.message}", e)
            null
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

    // --- Fruit CRUD ---

    suspend fun getAllFruits(): List<Fruit> {
        return try {
            val snapshot = fruitsCollection.get().await()
            snapshot.toObjects(Fruit::class.java)
        } catch (e: Exception) {
            Log.e(tag, "Error getting all fruits: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun addFruit(fruit: Fruit): Boolean {
        return try {
            fruitsCollection.document(fruit.id).set(fruit).await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error adding fruit: ${e.message}", e)
            false
        }
    }

    suspend fun updateFruit(fruit: Fruit): Boolean {
        return try {
            fruitsCollection.document(fruit.id).set(fruit).await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error updating fruit: ${e.message}", e)
            false
        }
    }

    suspend fun deleteFruit(fruitId: String): Boolean {
        return try {
            fruitsCollection.document(fruitId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error deleting fruit: ${e.message}", e)
            false
        }
    }

    suspend fun seedFruits(fruits: List<Fruit>): Boolean {
        return try {
            val snapshot = fruitsCollection.get().await()
            val needsMigration = snapshot.documents.any { !it.contains("price") || !it.contains("id") }

            if (needsMigration || snapshot.isEmpty) {
                Log.d(tag, "Seeding/Migrating fruits...")
                // Clear old data if migrating
                for (doc in snapshot.documents) {
                    doc.reference.delete().await()
                }
                // Add new data
                for (fruit in fruits) {
                    fruitsCollection.document(fruit.id).set(fruit).await()
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

    // --- Cart Management (Per User) ---

    suspend fun getCartItems(userId: String): List<CartItem> {
        return try {
            val snapshot = usersCollection.document(userId).collection("cartItems").get().await()
            snapshot.toObjects(CartItem::class.java)
        } catch (e: Exception) {
            Log.e(tag, "Error getting cart items: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun addToCart(userId: String, item: CartItem): Boolean {
        return try {
            val cartRef = usersCollection.document(userId).collection("cartItems").document(item.fruitId)
            val existing = cartRef.get().await()
            if (existing.exists()) {
                val currentQty = existing.getLong("quantity") ?: 0
                cartRef.update("quantity", currentQty + item.quantity).await()
            } else {
                cartRef.set(item).await()
            }
            true
        } catch (e: Exception) {
            Log.e(tag, "Error adding to cart: ${e.message}", e)
            false
        }
    }

    suspend fun updateCartItemQuantity(userId: String, fruitId: String, quantity: Int): Boolean {
        return try {
            if (quantity <= 0) {
                removeFromCart(userId, fruitId)
            } else {
                usersCollection.document(userId).collection("cartItems").document(fruitId)
                    .update("quantity", quantity).await()
            }
            true
        } catch (e: Exception) {
            Log.e(tag, "Error updating cart quantity: ${e.message}", e)
            false
        }
    }

    suspend fun removeFromCart(userId: String, fruitId: String): Boolean {
        return try {
            usersCollection.document(userId).collection("cartItems").document(fruitId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error removing from cart: ${e.message}", e)
            false
        }
    }

    suspend fun clearCart(userId: String): Boolean {
        return try {
            val cartItems = usersCollection.document(userId).collection("cartItems").get().await()
            for (doc in cartItems.documents) {
                doc.reference.delete().await()
            }
            true
        } catch (e: Exception) {
            Log.e(tag, "Error clearing cart: ${e.message}", e)
            false
        }
    }

    // --- Orders ---

    suspend fun placeOrder(order: Order): Boolean {
        return try {
            val orderRef = ordersCollection.document()
            val orderWithId = order.copy(id = orderRef.id)
            orderRef.set(orderWithId).await()
            clearCart(order.userId)
            true
        } catch (e: Exception) {
            Log.e(tag, "Error placing order: ${e.message}", e)
            false
        }
    }

    suspend fun getOrderHistory(userId: String): List<Order> {
        return try {
            val snapshot = ordersCollection.whereEqualTo("userId", userId).get().await()
            snapshot.toObjects(Order::class.java).sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            Log.e(tag, "Error getting order history: ${e.message}", e)
            emptyList()
        }
    }

    // --- Favorites ---

    suspend fun toggleFavorite(userId: String, fruitId: String): Boolean {
        return try {
            val favRef = usersCollection.document(userId).collection("favorites").document(fruitId)
            val exists = favRef.get().await().exists()
            if (exists) {
                favRef.delete().await()
            } else {
                favRef.set(mapOf("fruitId" to fruitId)).await()
            }
            true
        } catch (e: Exception) {
            Log.e(tag, "Error toggling favorite: ${e.message}", e)
            false
        }
    }

    suspend fun isFavorite(userId: String, fruitId: String): Boolean {
        return try {
            usersCollection.document(userId).collection("favorites").document(fruitId).get().await().exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFavoriteFruitIds(userId: String): List<String> {
        return try {
            val snapshot = usersCollection.document(userId).collection("favorites").get().await()
            snapshot.documents.map { it.id }
        } catch (e: Exception) {
            emptyList()
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
