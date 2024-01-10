package com.kuliah.pkm.tajwidify.ui.materi.submateri

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuliah.pkm.tajwidify.adapter.SubMateriAdapter
import com.kuliah.pkm.tajwidify.data.SubMateri
import com.kuliah.pkm.tajwidify.databinding.FragmentSubMateriBinding
import com.kuliah.pkm.tajwidify.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SubMateriFragment : Fragment() {

    private lateinit var binding: FragmentSubMateriBinding
    private lateinit var subMateriAdapter: SubMateriAdapter
    private val viewModel: SubMateriViewModel by viewModels()
    private lateinit var subMateri: SubMateri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubMateriBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subMateri = arguments?.getParcelable("SubMateri") ?: SubMateri()
        setupRv()
        observeSubMateri()
    }

    private fun observeSubMateri() {
        val category = arguments?.let { SubMateriFragmentArgs.fromBundle(it).category } ?: ""

        viewModel.fetchSubMateri(category)

        lifecycleScope.launch {
            viewModel.subMateriList.collectLatest { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        subMateriAdapter.differ.submitList(resource.data)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("SubMateri", resource.message.toString())
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setupRv() {
        subMateriAdapter = SubMateriAdapter(requireContext())

        binding.rvSubMateri.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = subMateriAdapter
        }

    }

}