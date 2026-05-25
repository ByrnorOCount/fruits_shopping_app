package com.mopr.fruits_app.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val addresses: List<Address> = emptyList(),
    @get:PropertyName("isAdmin")
    var isAdmin: Boolean = false
) : Parcelable
