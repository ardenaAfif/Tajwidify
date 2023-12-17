package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doa(
    val judul: String,
    val doaArab: String,
    val doaLatin: String,
    val arti: String
) : Parcelable {
    constructor(): this("", "", "", "")
}
