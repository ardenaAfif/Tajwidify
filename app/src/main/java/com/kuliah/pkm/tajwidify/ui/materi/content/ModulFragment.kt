package com.kuliah.pkm.tajwidify.ui.materi.content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import com.kuliah.pkm.tajwidify.databinding.FragmentModulBinding

@AndroidEntryPoint
class ModulFragment : Fragment() {

    private lateinit var binding: com.kuliah.pkm.tajwidify.databinding.FragmentModulBinding
    private val viewModel by viewModels<ModulViewModel>()
    private val args: ModulFragmentArgs by navArgs()

    private var currentContentPage: Int = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModulBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customToolbar()
        setupMateriContent()
        binding.apply {
            loadNextPage()
        }
    }

    private fun setupMateriContent() {
        val subModul = args.Modul
//        viewModel.fetchContent(subModul.subMateriId, subModul.subMateriId, currentContentPage)

        lifecycleScope.launchWhenStarted {
            viewModel.modulContent.collectLatest { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        resource.data?.let { materiContents ->
                            val content = materiContents.firstOrNull()
                            binding.apply{
                                titleContent.text = content?.title ?: "Judul tidak tersedia"
                                tvMateri.text = content?.materi ?: "Materi tidak tersedia"
                            }
                        }
                    }
                    is Resource.Error -> {
                        showLoading(true)
                        Log.e("Sub Materi", resource.message.toString())
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }


    private fun loadNextPage() {
        currentContentPage++
        val subModul = args.Modul
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