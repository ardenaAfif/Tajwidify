package com.kuliah.pkm.tajwidify.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirebaseCommon {

    private val firestore = FirebaseFirestore.getInstance()

     suspend fun getSubMateri(subMateriId: String): QuerySnapshot {
        return firestore.collection("modul")
            .document(subMateriId)
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
}