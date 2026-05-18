package com.mopr.fruits_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean = false
) : Parcelable