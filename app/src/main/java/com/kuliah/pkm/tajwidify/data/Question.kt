package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    @DocumentId
    val questionId: String,
    val question: String,
    val answer: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val time: Long
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", 0L)
}
