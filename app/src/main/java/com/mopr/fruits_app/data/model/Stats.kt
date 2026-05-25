package com.mopr.fruits_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminStats (
    val totalRevenue: Double = 0.0,
    val totalOrders: Int = 0,
    val topSellingFruits: List<String> = emptyList()
) : Parcelable{
}