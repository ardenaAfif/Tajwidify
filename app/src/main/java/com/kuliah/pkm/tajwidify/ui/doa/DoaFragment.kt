package com.kuliah.pkm.tajwidify.ui.doa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentDoaBinding

class DoaFragment : Fragment() {

    private lateinit var binding: FragmentDoaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoaBinding.inflate(layoutInflater)
        return binding.root
    }

}