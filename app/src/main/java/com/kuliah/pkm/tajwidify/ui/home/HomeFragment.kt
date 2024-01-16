package com.kuliah.pkm.tajwidify.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.adapter.MateriAdapter
import com.kuliah.pkm.tajwidify.databinding.FragmentHomeBinding
import com.kuliah.pkm.tajwidify.utils.Resource
import com.kuliah.pkm.tajwidify.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val userViewModel by viewModels<ProfileViewModel>()

    private val materiViewModel by viewModels<HomeViewModel>()
    private lateinit var materiAdapter: MateriAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRvMateri()
        materiSetup()
        getUser()
        onQuizCompleted()
    }

    private fun onQuizCompleted() {
        materiViewModel.refreshMateriList()
    }

    private fun setupRvMateri() {
        materiAdapter = MateriAdapter(requireContext())
        binding.rvMateri.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = materiAdapter
        }
    }

    private fun materiSetup() {
        lifecycleScope.launchWhenStarted {
            materiViewModel.modulList.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        hideLoading()
                        materiAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("Materi List", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getUser() {
        lifecycleScope.launchWhenStarted {
            userViewModel.user.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        binding.firstName.text = it.data?.firstName

                        val imagePath = it.data?.imagePath
                        if (imagePath.isNullOrEmpty()) {
                            Glide.with(requireContext())
                                .load(R.drawable.ic_default_profile)
                                .into(binding.ivProfile)
                        } else {
                            Glide.with(requireContext())
                                .load(imagePath)
                                .into(binding.ivProfile)
                        }
                    }
                    is Resource.Error -> {
                        Log.e("TAG", it.message.toString())
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

}