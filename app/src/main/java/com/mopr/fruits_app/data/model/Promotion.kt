package com.mopr.fruits_app.data.model

import android.accessibilityservice.GestureDescription
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promotion (
    val id: String = "",
    val code: String = "",
    val discountPercent: Int = 0,
    val description: String = "",
    val active: Boolean = true
) : Parcelable{
}