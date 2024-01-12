package com.kuliah.pkm.tajwidify.ui.materi.submateri

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuliah.pkm.tajwidify.adapter.SubMateriAdapter
import com.kuliah.pkm.tajwidify.databinding.FragmentSubMateriBinding
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SubMateriFragment : Fragment() {

    private lateinit var binding: FragmentSubMateriBinding
    private lateinit var subMateriAdapter: SubMateriAdapter
    private val viewModel by viewModels<SubMateriViewModel>()
    private val args: SubMateriFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubMateriBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val materi = args.subMateri
        viewModel.setSubMateri(materi)
        setupRvSubMateri()
        subMateriSetup()
        customToolbar()
    }

    private fun customToolbar() {
        val materi = args.subMateri

        binding.apply {
            toolbar.navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.tvToolbarName.text = materi.title
        }
    }

    private fun subMateriSetup() {
        lifecycleScope.launchWhenStarted {
            viewModel.subModulList.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        hideLoading()
                        subMateriAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("Sub Materi", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupRvSubMateri() {
        subMateriAdapter = SubMateriAdapter(requireContext())

        binding.rvSubMateri.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = subMateriAdapter
        }

    }

    private fun showLoading() {
        binding.apply {
            shimmerFrame.visibility = View.VISIBLE
            shimmerFrame.startShimmer()
        }
    }

    private fun hideLoading() {
        binding.apply{
            shimmerFrame.stopShimmer()
            shimmerFrame.visibility = View.GONE
        }
    }

}