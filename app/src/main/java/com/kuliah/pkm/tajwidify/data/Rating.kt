package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val rating: Long,
    val message: String
) : Parcelable