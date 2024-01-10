package com.kuliah.pkm.tajwidify.ui.materi.submateri

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.pkm.tajwidify.data.Materi
import com.kuliah.pkm.tajwidify.data.SubMateri
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

    private val _subMateriList = MutableStateFlow<Resource<List<SubMateri>>>(Resource.Unspecified())
    val subMateriList: StateFlow<Resource<List<SubMateri>>> = _subMateriList

    var materi: Materi? = null

//    init {
//        fetchSubMateri()
//    }

    fun setSubMateri(materi: Materi) {
        this.materi = materi
        fetchSubMateri()
    }

    private fun fetchSubMateri() {
        materi?.let {
            viewModelScope.launch {
                _subMateriList.emit(Resource.Loading())
                try {
                    val result = firebaseCommon.getSubMateri(materi!!.materiId)
                    val subMateriList = result.toObjects(SubMateri::class.java)
                    _subMateriList.emit(Resource.Success(subMateriList))
                } catch (e: Exception) {
                    _subMateriList.emit(Resource.Error(e.message ?: "Error occurred"))
                }
            }
        }

    }
}