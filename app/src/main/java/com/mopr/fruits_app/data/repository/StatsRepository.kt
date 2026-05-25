package com.mopr.fruits_app.data.repository

import com.mopr.fruits_app.data.model.AdminStats

class StatsRepository() {
    suspend fun getGeneralStats() = AdminStats()
}