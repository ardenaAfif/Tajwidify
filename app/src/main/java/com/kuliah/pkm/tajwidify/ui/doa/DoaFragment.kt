package com.kuliah.pkm.tajwidify.ui.doa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.adapter.DoaAdapter
import com.kuliah.pkm.tajwidify.databinding.FragmentDoaBinding
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DoaFragment : Fragment() {

    private lateinit var binding: FragmentDoaBinding
    private lateinit var doaAdapter: DoaAdapter
    private val doaViewModel by viewModels<DoaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customToolbar()
        searchAction()
        fetchDoaAction()
        setupRvDoa()
    }

    private fun customToolbar() {
        binding.apply {
            toolbar.navBack.isInvisible = true
            toolbar.tvToolbarName.text = getString(R.string.doa_harian)
        }
    }

    private fun setupRvDoa() {
        doaAdapter = DoaAdapter(requireContext())
        binding.rvDoa.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = doaAdapter
        }
    }

    private fun fetchDoaAction() {
        lifecycleScope.launchWhenStarted {
            doaViewModel.doa.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        doaAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("TAG", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
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

    private fun searchAction() {
        binding.apply {
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    performSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    performSearch(newText)
                    return true
                }

            })
        }
    }

    private fun performSearch(query: String?) {
        val filteredDoa = if (!query.isNullOrBlank()) {
            doaViewModel.doa.value.data?.filter { doa ->
                doa.judul.contains(query, ignoreCase = true)
            } ?: emptyList()
        } else {
            // Jika query kosong, tampilkan semua produk
            doaViewModel.doa.value.data ?: emptyList()
        }

        if (filteredDoa.isEmpty()) {
            binding.tvNoResults.visibility = View.VISIBLE
            binding.tvNoResults.text = getString(R.string.no_results)
        } else {
            binding.tvNoResults.visibility = View.GONE
        }

        // Perbarui daftar produk yang ditampilkan dalam RecyclerView
        doaAdapter.differ.submitList(filteredDoa)

    }

}