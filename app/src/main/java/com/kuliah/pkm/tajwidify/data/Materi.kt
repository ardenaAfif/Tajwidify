package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Materi(
    @DocumentId
    val materiId: String,
    val imgUrl: String,
    val title: String,
    val category: String,
    val urutan: Long
) : Parcelable {
    constructor() : this("", "","", "", 0L)
}