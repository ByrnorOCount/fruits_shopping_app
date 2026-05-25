package com.mopr.fruits_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment (
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val fruitId: String = "",
    val text: String = "",
    val rating: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable