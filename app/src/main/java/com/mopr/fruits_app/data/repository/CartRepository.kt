package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.CartItem

class CartRepository(private val firestore: FirestoreManager) {
    suspend fun getCart(userId: String) = firestore.getCartItems(userId)
    suspend fun addToCart(userId: String, item: CartItem) = firestore.addToCart(userId, item)
    suspend fun removeFromCart(userId: String, fruitId: String) = firestore.removeFromCart(userId, fruitId)
}