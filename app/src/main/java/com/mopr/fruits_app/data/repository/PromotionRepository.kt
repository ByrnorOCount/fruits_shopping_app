package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.model.Promotion

class PromotionRepository() {
    suspend fun getActivePromotions() = emptyList<Promotion>()
    suspend fun validatePromoCode(code: String) = null
}