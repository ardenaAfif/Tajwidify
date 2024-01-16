package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class MateriContent(
    @DocumentId
    val materiContentId: String,
    val page: Long,
    val title: String,
    val materi: String,
    val audioUrl: List<String>,
    val huruf: List<String>
) : Parcelable {
    constructor() : this("", 0L, "", "", audioUrl = emptyList(), huruf = emptyList())
}
