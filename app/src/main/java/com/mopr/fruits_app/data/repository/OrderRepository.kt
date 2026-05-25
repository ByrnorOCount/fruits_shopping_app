package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.Order

class OrderRepository(private val firestore: FirestoreManager) {
    suspend fun placeOrder(order: Order) = firestore.placeOrder(order)
    suspend fun getHistory(userId: String) = firestore.getOrderHistory(userId)

    suspend fun cancelOrder(orderId: String) = firestore.cancelOrder(orderId)
}