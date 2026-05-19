package com.mopr.fruits_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fruit(
    val name: String = "",
    val imageRes: Int = 0,
    val description: String = "",
    val scientificName: String = "",
    val healthBenefits: String = "",
) : Parcelable