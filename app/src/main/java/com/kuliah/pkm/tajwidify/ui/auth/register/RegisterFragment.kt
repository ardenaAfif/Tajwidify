package com.kuliah.pkm.tajwidify.ui.auth.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.data.User
import com.kuliah.pkm.tajwidify.databinding.FragmentRegisterBinding
import com.kuliah.pkm.tajwidify.utils.RegisterValidation
import com.kuliah.pkm.tajwidify.utils.Resource
import com.kuliah.pkm.tajwidify.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            // Button Sign Up action
            btnRegister.setOnClickListener {
                val user = User(
                    etFirstName.text.toString().trim(),
                    etLastName.text.toString().trim(),
                    etEmail.text.toString().trim()
                )
                val password = etPassword.text.toString()

                if (user != null && password.isNotEmpty()) {
                    viewModel.createAccountWithEmailAndPassword(user, password)
                    Toast.makeText(requireContext(), "Berhasil membuat akun baru", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Silahkan isi data terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Have an account (provide to Login)
            haveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                        binding.btnRegister.revertAnimation()
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.btnRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.empty is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.empty.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (validation.firstName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.firstName.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (validation.lastName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.lastName.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.email.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            validation.password.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "RegisterFragment"
    }
}