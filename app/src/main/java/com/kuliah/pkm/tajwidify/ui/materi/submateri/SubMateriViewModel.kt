package com.kuliah.pkm.tajwidify.ui.materi.submateri

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.pkm.tajwidify.data.Modul
import com.kuliah.pkm.tajwidify.data.SubModul
import com.kuliah.pkm.tajwidify.firebase.FirebaseCommon
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubMateriViewModel @Inject constructor(
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _subModulList = MutableStateFlow<Resource<List<SubModul>>>(Resource.Unspecified())
    val subModulList: StateFlow<Resource<List<SubModul>>> = _subModulList

    var modul: Modul? = null

    fun setSubMateri(modul: Modul) {
        this.modul = modul
        fetchSubMateri()
    }

    private fun fetchSubMateri() {
        modul?.let {
            viewModelScope.launch {
                _subModulList.emit(Resource.Loading())
                try {
                    val result = firebaseCommon.getSubMateri(modul!!.materiId)
                    val subModulList = result.toObjects(SubModul::class.java)
                    _subModulList.emit(Resource.Success(subModulList))
                } catch (e: Exception) {
                    _subModulList.emit(Resource.Error(e.message ?: "Error occurred"))
                }
            }
        }

    }
}