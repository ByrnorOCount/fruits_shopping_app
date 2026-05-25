package com.mopr.fruits_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val timestamp: Long = 0,
    val status: String = "Completed",
    val address: Address? = null,
    val paymentMethod: String = ""
) : Parcelable