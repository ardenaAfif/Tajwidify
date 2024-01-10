package com.kuliah.pkm.tajwidify.ui.doa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.pkm.tajwidify.data.Doa
import com.kuliah.pkm.tajwidify.firebase.FirebaseCommon
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoaViewModel @Inject constructor(
    private val firestore: FirebaseCommon
) : ViewModel() {

    private val _doa = MutableStateFlow<Resource<List<Doa>>>(Resource.Unspecified())
    val doa: StateFlow<Resource<List<Doa>>> = _doa

    init {
        fetchDoa()
    }

    private fun fetchDoa() {
        viewModelScope.launch {
            _doa.emit(Resource.Loading())
            try {
                val result = firestore.getDoa()
                val doaList = result.toObjects(Doa::class.java)
                _doa.emit(Resource.Success(doaList))
            } catch (e: Exception) {
                _doa.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}