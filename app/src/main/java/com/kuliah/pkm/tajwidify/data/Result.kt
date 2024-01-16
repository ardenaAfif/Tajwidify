package com.kuliah.pkm.tajwidify.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    @DocumentId
    val user_id: String,
    val quizId: String,
    val title: String,
    val correct: Long,
    val unanswered: Long,
    val wrong: Long
): Parcelable {
    constructor() : this("", "", "", 0L, 0L, 0L)
}
