package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.Comment
import com.mopr.fruits_app.data.model.Fruit

class FruitRepository(private val firestore: FirestoreManager) {
    suspend fun getFruits() = firestore.getAllFruits()
    suspend fun addFruit(fruit: Fruit) = firestore.addFruit(fruit)
    suspend fun updateFruit(fruit: Fruit) = firestore.updateFruit(fruit)
    suspend fun deleteFruit(id: String) = firestore.deleteFruit(id)

    // New Feature Placeholders
    suspend fun getRelatedProducts(fruitId: String) = emptyList<Fruit>()
    suspend fun getComments(fruitId: String) = emptyList<Comment>()
    suspend fun addComment(comment: Comment) = true
}