package com.kuliah.pkm.tajwidify.ui.quiz

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.toObject
import com.kuliah.pkm.tajwidify.data.Modul
import com.kuliah.pkm.tajwidify.data.Question
import com.kuliah.pkm.tajwidify.firebase.FirebaseCommon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "QuizViewModel"

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    lateinit var quizTajwid: Modul
    private var totalQuestionToAnswer: Long = 0

    private var allQuestionList = mutableListOf<Question>()
    private val questionToAnswer = mutableListOf<Question>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Question Detail
    private val _quizTitle = MutableLiveData<String>()
    val quizTitle: LiveData<String> get() = _quizTitle

    private val _questionNumber = MutableLiveData<Int>()
    val questionNumber: LiveData<Int> get() = _questionNumber

    private val _questionsTotalNumber = MutableLiveData<Long>()
    val questionTotalNumber: LiveData<Long> get() = _questionsTotalNumber

    private val _questionText = MutableLiveData<String>()
    val questionText: LiveData<String> get() = _questionText

    // Timer
    private val _questionTime = MutableLiveData<String>()
    val questionTime: LiveData<String> get() = _questionTime

    private val _questionProgress = MutableLiveData<Int>()
    val questionProgress: LiveData<Int> get() = _questionProgress

    private val _isTimeUp = MutableLiveData<Boolean>()
    val isTimeUp: LiveData<Boolean> get() = _isTimeUp

    // Question Timer
    private lateinit var timer: CountDownTimer

    // Navigation
    private val _shouldNavigateToResult = MutableLiveData<Boolean>()
    val shouldNavigateToResult: LiveData<Boolean> get() = _shouldNavigateToResult


    // Options
    private val _optionA = MutableLiveData<String>()
    val optionA: LiveData<String> get() = _optionA

    private val _optionB = MutableLiveData<String>()
    val optionB: LiveData<String> get() = _optionB

    private val _optionC = MutableLiveData<String>()
    val optionC: LiveData<String> get() = _optionC

    private val _optionD = MutableLiveData<String>()
    val optionD: LiveData<String> get() = _optionD

    // Can Answer
    private var canAnswer: Boolean = false
    private var correctAnswer: Int = 0
    private var wrongAnswer: Int = 0
    private var currentQuestionNumber: Int = 1
    private var notAnswered: Int = 0

    fun initializeQuestion(quizListModel: Modul) {
        uiScope.launch {
            isLoading(true)

            quizTajwid = quizListModel
            totalQuestionToAnswer = quizListModel.question

            _quizTitle.value = quizListModel.title
            _questionsTotalNumber.value = quizListModel.question

            fetchQuestions()

            isLoading(false)
        }
    }

    private suspend fun fetchQuestions() {
        withContext(Dispatchers.IO) {
            val value = firebaseCommon.getQuizQuestion(quizTajwid.materiId)

            val questionModelList: MutableList<Question> = mutableListOf()
            for (doc in value!!) {
                val questionItem = doc.toObject<Question>()
                questionModelList.add(questionItem)
            }
            allQuestionList = questionModelList
        }

        pickQuestion()
        loadQuestion(currentQuestionNumber)
    }

    private fun pickQuestion() {
        for (i in 0 until totalQuestionToAnswer.toInt()) {
            val randomNumber = getRandomInteger(allQuestionList.size)
            questionToAnswer.add(allQuestionList[randomNumber])
            allQuestionList.removeAt(randomNumber)

            Log.d(TAG, "Question $i" + questionToAnswer[i].question)
        }
    }

    private fun getRandomInteger(max: Int, min: Int = 0): Int {
        return ((Math.random() * (max - min))).toInt() + min
    }

    private fun loadQuestion(questionNumber: Int) {
        // Question number
        _questionNumber.value = questionNumber

        // Load Question
        _questionText.value = questionToAnswer[questionNumber - 1].question

        shuffleChoices(questionNumber)

        // Question Loaded, set can Answer
        canAnswer = true
        currentQuestionNumber = questionNumber

        // Start Question timer
        startTimer(questionNumber)
    }

    private fun startTimer(questionNumber: Int) {

        // Set timer text
        val timeToAnswer = questionToAnswer[questionNumber - 1].time
        _questionTime.value = timeToAnswer.toString()

        // Start Countdown
        timer = object : CountDownTimer(timeToAnswer + 15000, 15) {
            override fun onTick(milisUntilFinished: Long) {
                _questionTime.value = (milisUntilFinished / 1000).toString()

                val percent = milisUntilFinished / (timeToAnswer * 10)
                _questionProgress.value = percent.toInt()
            }

            override fun onFinish() {
                // Time is up
                canAnswer = false
                notAnswered++
                onTimeUp()
            }
        }
        timer.start()
    }

    private fun shuffleChoices(questionNumber: Int) {

        var answers = listOf(
            questionToAnswer[questionNumber - 1].optionA,
            questionToAnswer[questionNumber - 1].optionB,
            questionToAnswer[questionNumber - 1].optionC,
            questionToAnswer[questionNumber - 1].optionD
        ).toMutableList()

        // Clear Choices
        _optionA.value = answers.getOrNull(0) ?: ""
        _optionB.value = answers.getOrNull(0) ?: ""
        _optionC.value = answers.getOrNull(0) ?: ""
        _optionD.value = answers.getOrNull(0) ?: ""

        answers = answers.filter { it.isNotEmpty() }.toMutableList()

        for (i in 0 until answers.size) {
            val j = (0..i).random()

            val temp: String = answers[i]
            answers[i] = answers[j]
            answers[j] = temp
        }

        mutableListOf(_optionA, _optionB, _optionC, _optionD).forEachIndexed { index, it ->
            if (index <= answers.size - 1) {
                it.value = answers[index]
            }
        }
    }


    fun getCorrectAnswer(selectedAnswer: String): String {
        // Check Answer
        if (canAnswer) {
            if (questionToAnswer[currentQuestionNumber - 1].answer == selectedAnswer) {
                // Correct Answer
                correctAnswer++
                Log.d(TAG, "Correct Answer")
            } else {
                wrongAnswer++
                Log.d(TAG, "Wrong Answer")
            }
            // Set can answer to false
            canAnswer = false
            // Stop timer
            timer.cancel()
        }
        return questionToAnswer[currentQuestionNumber - 1].answer
    }

    fun loadNextQuestion() {
        currentQuestionNumber++

        if (currentQuestionNumber > totalQuestionToAnswer) {
            submitResults()
        } else {
            loadQuestion(currentQuestionNumber)
        }
    }

    fun navigateToResultPageComplete() {
        _shouldNavigateToResult.value = false
    }

    private fun submitResults() {
        uiScope.launch {
            isLoading(true)

            val resultMap = HashMap<String, Any?>()

            resultMap["correct"] = correctAnswer
            resultMap["wrong"] = wrongAnswer
            resultMap["unanswered"] = notAnswered

            resultMap["title"] = quizTajwid.title
            resultMap["urutan"] = quizTajwid.urutan

            submit(resultMap)
        }
    }

    private suspend fun submit(resultMap: java.util.HashMap<String, Any?>) {
        withContext(Dispatchers.IO) {
            try {
                firebaseCommon.submitQuizResult(
                    quizTajwid.materiId,
                    resultMap
                )
                navigateToResultPage()
            } catch (e: Exception) {
                _quizTitle.postValue(e.message)
            }
        }
    }

    fun onTimeUpComplete() {
        _isTimeUp.value = false
    }

    fun canAnswer(): Boolean {
        return canAnswer
    }

    private fun onTimeUp() {
        _isTimeUp.value = true
    }

    private fun isLoading(bool: Boolean) {
        _isLoading.value = bool
    }

    private fun navigateToResultPage() {
        _shouldNavigateToResult.postValue(true)
    }
}