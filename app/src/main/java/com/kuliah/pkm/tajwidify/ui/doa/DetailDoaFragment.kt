package com.kuliah.pkm.tajwidify.ui.doa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kuliah.pkm.tajwidify.databinding.FragmentDetailDoaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailDoaFragment : Fragment() {

    private lateinit var binding: FragmentDetailDoaBinding
    private val args by navArgs<DetailDoaFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailDoaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customToolbar()
        retrieveDoa()


    }

    private fun customToolbar() {
        binding.toolbar.navBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun retrieveDoa() {
        val doa = args.doa

        binding.apply {
            toolbar.tvToolbarName.text = doa.judul
            tvDoaArab.text = doa.doaArab
            tvDoaLatin.text = doa.doaLatin
            tvArti.text = doa.arti
        }
    }

}