package com.kuliah.pkm.tajwidify.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kuliah.pkm.tajwidify.data.MateriContent
import kotlinx.coroutines.tasks.await

class FirebaseCommon {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getQuizQuestion(quizId: String): QuerySnapshot? {
        return firestore.collection("modul")
            .document(quizId)
            .collection("quiz")
            .get().await()
    }

    suspend fun submitQuizResult(
        quizId: String,
        result:HashMap<String, Any?>
    ): Void? {
        firestore.collection("modul").document(quizId)
            .update("isPretest", true).await()

        return firestore.collection("modul").document(quizId)
            .collection("results").document().set(result).await()
    }

     suspend fun getSubMateri(materiId: String): QuerySnapshot {
        return firestore.collection("modul")
            .document(materiId)
            .collection("subModul")
            .orderBy("urutan")
            .get().await()
    }

    suspend fun getMateriList(): QuerySnapshot {
        return firestore.collection("modul")
            .orderBy("urutan")
            .get().await()
    }

    suspend fun getDoa(): QuerySnapshot {
        return firestore.collection("doa")
            .get().await()
    }


    suspend fun getResultsById(quizId: String): DocumentSnapshot? {
        return firestore.collection("mutholaah").document(quizId)
            .collection("results").document()
            .get().await()
    }
}