package com.mopr.fruits_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address (
    val id: String = "",
    val userId: String = "",
    val name: String = "", // e.g., Home, Work
    val street: String = "",
    val city: String = "",
    val zipCode: String = "",
    val isDefault: Boolean = false
) : Parcelable