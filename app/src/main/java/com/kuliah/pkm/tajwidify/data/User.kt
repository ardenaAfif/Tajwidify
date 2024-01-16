package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val firstName: String,
    val lastName: String,
    val email: String,
    val imagePath: String = ""
) : Parcelable {
    constructor(): this("", "", "", "")
}