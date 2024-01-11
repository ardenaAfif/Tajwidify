package com.kuliah.pkm.tajwidify.ui.profile.privacy

import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentPrivacyBinding

class PrivacyFragment : Fragment() {

    private lateinit var binding: FragmentPrivacyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacyBinding.inflate(layoutInflater)
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
            tvToolbarName.text = getString(R.string.privacy)
        }
    }
}