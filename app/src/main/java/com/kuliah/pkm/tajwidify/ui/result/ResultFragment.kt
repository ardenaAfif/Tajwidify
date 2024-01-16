package com.kuliah.pkm.tajwidify.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentResultBinding

class ResultFragment : Fragment() {


    private lateinit var binding: FragmentResultBinding
    private var correct: Int = 0

    private val args: ResultFragmentArgs by navArgs()
    private val resultViewModel: ResultViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        resultViewModel.fetchQuizResult(args.Modul)

        binding = FragmentResultBinding.inflate(inflater, container, false).apply {
            viewModel = resultViewModel
            lifecycleOwner = viewLifecycleOwner
            quizDetail = args.Modul
        }

        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        resultViewModel.correctScore.observe(viewLifecycleOwner, Observer {
            binding.resultsContent.startAnimation(fadeInAnimation)

            binding.resultsScore.text = getString(R.string.score_over, it, args.Modul.question)

            correct = it
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = args.Modul

        binding.apply {
            tajwidText.text = result.title
            btnBelajar.setOnClickListener {
                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToSubMateriFragment(result))
            }
        }
    }

}