package com.mopr.fruits_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val fruitId: String = "",
    val fruitName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val imageRes: Int = 0
) : Parcelable