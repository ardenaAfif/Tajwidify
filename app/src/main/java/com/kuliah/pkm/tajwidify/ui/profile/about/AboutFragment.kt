package com.kuliah.pkm.tajwidify.ui.profile.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customToolbar()
    }

    private fun customToolbar() {
        binding.toolbar.apply {
            navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            tvToolbarName.text = getString(R.string.tentang_kami)
        }
    }

}