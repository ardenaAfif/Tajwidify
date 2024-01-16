package com.kuliah.pkm.tajwidify.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.toObject
import com.kuliah.pkm.tajwidify.data.Modul
import com.kuliah.pkm.tajwidify.data.Result
import com.kuliah.pkm.tajwidify.firebase.FirebaseCommon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _scoreProgress = MutableLiveData<Int>()
    val scoreProgress: LiveData<Int> get() = _scoreProgress

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> get() = _result

    private val _correctScore = MutableLiveData<Int>()
    val correctScore: LiveData<Int> get() = _correctScore

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchQuizResult(quizListModel: Modul) {
        uiScope.launch {
            _isLoading.value = true
            getResult(quizListModel)
            _isLoading.value = false
        }
    }

    private suspend fun getResult(quizListModel: Modul) {
        withContext(Dispatchers.IO) {
            val value = firebaseCommon.getResultsById(quizListModel.materiId)

            val result = value?.toObject<Result>()

            if (result != null) {
                val correct = result.correct
                val wrong = result.wrong
                val missed = result.unanswered

                _correctScore.postValue(correct.toInt())
                val total = correct + wrong + missed

                val percent = (correct * 100) / total
                _scoreProgress.postValue(percent.toInt())

                val passed = correct > (total / 2)
                _result.postValue(passed)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}