package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.AdminStats

class StatsRepository(private val firestore: FirestoreManager) {
    suspend fun getGeneralStats() = firestore.getAdminStats()
}