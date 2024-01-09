package com.kuliah.pkm.tajwidify.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentHomeBinding
import com.kuliah.pkm.tajwidify.utils.Resource
import com.kuliah.pkm.tajwidify.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val userViewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
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

}