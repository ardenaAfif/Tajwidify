package com.kuliah.pkm.tajwidify.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentLoginBinding
import com.kuliah.pkm.tajwidify.ui.home.HomeActivity
import com.kuliah.pkm.tajwidify.utils.Resource
import com.kuliah.pkm.tajwidify.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            // Button login action
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(email, password)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Harap isi email dan password dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnGoogle.setOnClickListener {
                Toast.makeText(requireContext(), "Fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }

            // Haven't account (provide to Register)
            dontHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            if (viewModel.isUserLoggedIn()){
                Intent(requireContext(), HomeActivity::class.java).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(it)
                }
            } else {
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.btnLogin.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.btnLogin.revertAnimation()
                            Intent(requireActivity(), HomeActivity::class.java).also {
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(it)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Email atau Password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.btnLogin.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

}