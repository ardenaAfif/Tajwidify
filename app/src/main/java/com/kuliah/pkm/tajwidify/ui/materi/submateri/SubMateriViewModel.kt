package com.kuliah.pkm.tajwidify.ui.materi.submateri

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.kuliah.pkm.tajwidify.data.SubMateri
import com.kuliah.pkm.tajwidify.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SubMateriViewModel : ViewModel() {

    private val _subMateriList = MutableStateFlow<Resource<List<SubMateri>>>(Resource.Unspecified())
    val subMateriList: StateFlow<Resource<List<SubMateri>>> = _subMateriList

    private val firestore = FirebaseFirestore.getInstance()
    private val subMateriCollection = firestore.collection("materi")

    fun fetchSubMateri(category: String) {
        viewModelScope.launch {
            _subMateriList.emit(Resource.Loading())
            try {
                val result = subMateriCollection.whereEqualTo("category", category).get().await()
                val subMateriList = result.toObjects(SubMateri::class.java)
                _subMateriList.emit(Resource.Success(subMateriList))
            } catch (e: Exception) {
                _subMateriList.emit(Resource.Error(e.message ?: "Error occurred"))
            }
        }
    }
}