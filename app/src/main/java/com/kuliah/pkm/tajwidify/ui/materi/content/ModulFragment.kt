package com.kuliah.pkm.tajwidify.ui.materi.content

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentModulBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModulFragment : Fragment() {

    private lateinit var binding: FragmentModulBinding
    private val args: ModulFragmentArgs by navArgs()

    private var currentPage: Int = 1
    private lateinit var pageNumbers: IntArray
    private lateinit var titles: Array<String>
    private lateinit var contents: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModulBinding.inflate(layoutInflater)

        loadDataBasedOnTitle(args.Modul.title)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customToolbar()
        updateContent()
        btnAction()
    }

    private fun loadDataBasedOnTitle(title: String) {
        when(title) {
            "Idhar Halqi" -> {
                titles = resources.getStringArray(R.array.title_idhar_halqi)
                contents = resources.getStringArray(R.array.materi_idhar_halqi)
            }
            "Iqlab" -> {
                titles = resources.getStringArray(R.array.title_iqlab)
                contents = resources.getStringArray(R.array.materi_iqlab)
            }
            "Idgham Bilagunnah" -> {
                titles = resources.getStringArray(R.array.title_idgham_bilagunnah)
                contents = resources.getStringArray(R.array.materi_idgham_bilagunnah)
            }
            "Idgham Bigunnah" -> {
                titles = resources.getStringArray(R.array.title_idgham_bigunnah)
                contents = resources.getStringArray(R.array.materi_idgham_bigunnah)
            }
            else -> {
                // Default atau error handling
                titles = arrayOf("Unknown Title")
                contents = arrayOf("No content available")
            }
        }
        pageNumbers = IntArray(titles.size) { it + 1 } // Menyesuaikan pageNumbers berdasarkan jumlah judul
    }

    private fun btnAction() {
        binding.apply {
            btnNext.setOnClickListener {
                if (currentPage < pageNumbers.size) {
                    currentPage++
                    updateContent()
                } else {
                    // Navigasi ke halaman SubMateri
                    findNavController().navigateUp()
                }
            }
            btnPrev.setOnClickListener {
                if (currentPage > 1) {
                    currentPage--
                    updateContent()
                }
            }
        }
    }

    private fun updateContent() {
        binding.apply {
            titleContent.text = titles[currentPage - 1]
            tvMateri.text = contents[currentPage - 1]

            // Atur visibility btnPrev
            binding.btnPrev.visibility = if (currentPage == 1) View.GONE else View.VISIBLE

            // Atur teks btnNext jika di halaman terakhir
            if (currentPage == pageNumbers.size) {
                tvBtnNext.text = "Kembali ke Menu"
                tvMateri.textSize = 21f
                tvMateri.gravity = Gravity.CENTER

            } else {
                tvBtnNext.text = "Lanjut"
                tvMateri.textSize = 15f
            }
        }
    }


    private fun customToolbar() {

        val modul = args.Modul

        binding.toolbar.apply {
            navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            tvToolbarName.text = modul.title
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}