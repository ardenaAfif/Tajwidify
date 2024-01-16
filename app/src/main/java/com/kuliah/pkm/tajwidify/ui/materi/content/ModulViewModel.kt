package com.kuliah.pkm.tajwidify.ui.materi.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.pkm.tajwidify.data.MateriContent
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
class ModulViewModel @Inject constructor(
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _modulContent = MutableStateFlow<Resource<List<MateriContent>>>(Resource.Unspecified())
    val modulContent: StateFlow<Resource<List<MateriContent>>> = _modulContent

    var modul: Modul? = null
    var subModul: SubModul? = null

    fun getContent(modul: Modul, subModul: SubModul) {

    }

    private fun fetchContent(modul: Modul, subModul: SubModul, page: Int) {
        viewModelScope.launch {
            _modulContent.emit(Resource.Loading())
            try {
                val result = firebaseCommon.getMateriContent(modul.materiId, subModul.subMateriId, page)
                val content = result.toObjects(MateriContent::class.java)
                _modulContent.emit(Resource.Success(content))
            } catch (e: Exception) {
                _modulContent.emit(Resource.Error(e.message ?: "Error Occurred"))
            }
        }
    }
}