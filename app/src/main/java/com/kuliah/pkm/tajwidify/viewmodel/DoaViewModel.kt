package com.kuliah.pkm.tajwidify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.kuliah.pkm.tajwidify.data.Doa
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoaViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _doa = MutableStateFlow<Resource<List<Doa>>>(Resource.Unspecified())
    val doa: StateFlow<Resource<List<Doa>>> = _doa

    init {
        fetchDoa()
    }

    private fun fetchDoa() {
        viewModelScope.launch {
            _doa.emit(Resource.Loading())
        }

        firestore.collection("doa").get()
            .addOnSuccessListener { result ->
                val doaList = result.toObjects(Doa::class.java)
                viewModelScope.launch {
                    _doa.emit(Resource.Success(doaList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _doa.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}