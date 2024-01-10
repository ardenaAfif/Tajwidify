package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Materi(
    @DocumentId
    val materiId: String,
    val isPretest: Boolean,
    val imgUrl: String,
    val question: Long,
    val title: String,
    val urutan: Int
) : Parcelable {
    constructor() : this("", false, "", 0L,"",0)
}
