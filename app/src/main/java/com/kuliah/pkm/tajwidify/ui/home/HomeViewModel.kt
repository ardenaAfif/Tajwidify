package com.kuliah.pkm.tajwidify.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.pkm.tajwidify.data.Materi
import com.kuliah.pkm.tajwidify.firebase.FirebaseCommon
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseCommon
) : ViewModel() {

    private val _materiList = MutableStateFlow<Resource<List<Materi>>>(Resource.Unspecified())
    val materiList: StateFlow<Resource<List<Materi>>> = _materiList


    init {
        fetchMateriList()
    }

    private fun fetchMateriList() {
        viewModelScope.launch {
            _materiList.emit(Resource.Loading())
            try {
                val result = firestore.getMateriList()
                val materiList = result.toObjects(Materi::class.java)
                _materiList.emit(Resource.Success(materiList))
            } catch (e: Exception) {
                _materiList.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}