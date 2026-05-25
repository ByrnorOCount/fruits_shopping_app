package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.Order

class OrderRepository(private val firestore: FirestoreManager) {
    suspend fun placeOrder(order: Order) = firestore.placeOrder(order)
    suspend fun getHistory(userId: String) = firestore.getOrderHistory(userId)

    // New Feature Placeholder
    suspend fun cancelOrder(orderId: String) = true
}