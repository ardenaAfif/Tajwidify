package com.kuliah.pkm.tajwidify.ui.quiz

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentQuizBinding
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.M)

@AndroidEntryPoint
class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding

    private val args: QuizFragmentArgs by navArgs()
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(layoutInflater)

        quizViewModel.initializeQuestion(args.Modul)

        binding = FragmentQuizBinding.inflate(inflater, container, false)
            .apply {
                viewModel = quizViewModel
                lifecycleOwner = viewLifecycleOwner
            }

        setButtonVisibility(binding.optionA, View.VISIBLE, true)
        setButtonVisibility(binding.optionB, View.VISIBLE, true)
        setButtonVisibility(binding.optionC, View.VISIBLE, true)
        setButtonVisibility(binding.optionD, View.VISIBLE, true)
        setButtonVisibility(binding.quizNextBtn, View.VISIBLE, false)

        binding.optionA.setOnClickListener {
            val btn = it as Button

            validateOptionSelected(quizViewModel, btn, binding)
        }

        binding.optionB.setOnClickListener {
            val btn = it as Button

            validateOptionSelected(quizViewModel, btn, binding)
        }

        binding.optionC.setOnClickListener {
            val btn = it as Button

            validateOptionSelected(quizViewModel, btn, binding)
        }

        binding.optionD.setOnClickListener {
            val btn = it as Button

            validateOptionSelected(quizViewModel, btn, binding)
        }

        binding.quizNextBtn.setOnClickListener {
            quizViewModel.loadNextQuestion()
            resetOptions(binding)
        }

        quizViewModel.isTimeUp.observe(viewLifecycleOwner, Observer {
            if (it) {

                // empty string no answer was selected
                val correctAnswer = quizViewModel.getCorrectAnswer("")
                highlightCorrectAnswer(correctAnswer)
                setButtonVisibility(binding.quizNextBtn, View.VISIBLE, true)
                quizViewModel.onTimeUpComplete()
            }
        })

        quizViewModel.shouldNavigateToResult.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
//                    .navigate(QuizFragmentDirections.actionQuizFragmentToResultFragment(args.Modul))
                quizViewModel.navigateToResultPageComplete()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customToolbar()
    }

    private fun customToolbar() {
        val quiz = args.Modul

        binding.toolbar.apply {
            navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            tvToolbarName.text = "Pre-Test ${quiz.title}"
        }
    }

    private fun resetOptions(binding: FragmentQuizBinding) {
        setButtonBackground(binding.optionA, null)
        setButtonBackground(binding.optionB, null)
        setButtonBackground(binding.optionC, null)
        setButtonBackground(binding.optionD, null)

        binding.optionA.setTextColor(resources.getColor(R.color.black, null))
        binding.optionB.setTextColor(resources.getColor(R.color.black, null))
        binding.optionC.setTextColor(resources.getColor(R.color.black, null))
        binding.optionD.setTextColor(resources.getColor(R.color.black, null))

        setButtonVisibility(binding.quizNextBtn, View.INVISIBLE, false)
    }

    private fun validateOptionSelected(
        quizViewModel: QuizViewModel,
        option: Button,
        binding: FragmentQuizBinding
    ) {
        if (quizViewModel.canAnswer()) {
            val correctAnswer = quizViewModel.getCorrectAnswer(option.text.toString())

            option.setTextColor(resources.getColor(R.color.white, null))

            val isCorrect = option.text == correctAnswer
            setButtonBackground(option, isCorrect)
            if (!isCorrect) {
                highlightCorrectAnswer(correctAnswer)
            }

            setButtonVisibility(binding.quizNextBtn, View.VISIBLE, true)
        }
    }

    private fun highlightCorrectAnswer(correctAnswer: String) {
        binding.apply {
            when (correctAnswer) {
                optionA.text -> {
                    setButtonBackground(optionA, true)
                    optionA.setTextColor(resources.getColor(R.color.white, null))
                }

                optionB.text -> {
                    setButtonBackground(optionB, true)
                    optionB.setTextColor(resources.getColor(R.color.white, null))
                }

                optionC.text -> {
                    setButtonBackground(optionC, true)
                    optionC.setTextColor(resources.getColor(R.color.white, null))
                }

                optionD.text -> {
                    setButtonBackground(optionD, true)
                    optionD.setTextColor(resources.getColor(R.color.white, null))
                }
            }
        }
    }

    private fun setButtonBackground(button: Button, isCorrect: Boolean?) {
        if (isCorrect != null && isCorrect) {
            button.backgroundTintList = resources.getColorStateList(R.color.colorCorrect, null)
        } else if (isCorrect != null && !isCorrect) {
            button.backgroundTintList = resources.getColorStateList(R.color.colorWrong, null)
        } else {
            button.backgroundTintList = resources.getColorStateList(R.color.white, null)
        }
    }

    private fun setButtonVisibility(button: Button, visible: Int, isEnabled: Boolean) {
        button.visibility = visible
        button.isEnabled = isEnabled
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}