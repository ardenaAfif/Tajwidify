package com.kuliah.pkm.tajwidify.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.pkm.tajwidify.data.Modul
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

    private val _modulList = MutableStateFlow<Resource<List<Modul>>>(Resource.Unspecified())
    val modulList: StateFlow<Resource<List<Modul>>> = _modulList


    init {
        fetchMateriList()
    }

    private fun fetchMateriList() {
        viewModelScope.launch {
            _modulList.emit(Resource.Loading())
            try {
                val result = firestore.getMateriList()
                val modulList = result.toObjects(Modul::class.java)
                _modulList.emit(Resource.Success(modulList))
            } catch (e: Exception) {
                _modulList.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}