package com.kuliah.pkm.tajwidify.ui.materi.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentModulBinding

class ModulFragment : Fragment() {

    private lateinit var binding: FragmentModulBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modul, container, false)
    }

}