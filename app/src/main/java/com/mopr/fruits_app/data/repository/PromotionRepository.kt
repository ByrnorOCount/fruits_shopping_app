package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.Promotion

class PromotionRepository(private val firestore: FirestoreManager) {
    suspend fun getActivePromotions() = emptyList<Promotion>() // Future: implementation
    suspend fun validatePromoCode(code: String) = firestore.validatePromoCode(code)
}