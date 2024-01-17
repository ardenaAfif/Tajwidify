package com.kuliah.pkm.tajwidify.ui.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentProfileBinding
import com.kuliah.pkm.tajwidify.ui.auth.AuthActivity
import com.kuliah.pkm.tajwidify.ui.profile.rating.RateUsDialog
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()
    private var currentImageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionMenu()
        userProfile()
        getVersion()
        getRating()

        passwordUpdateSetup()
    }

    private fun getRating() {
        binding.btnRate.setOnClickListener {
            showRatingDialog()
        }
    }

    private fun passwordUpdateSetup() {
        viewModel.passwordUpdateStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Error -> {
                    Toast.makeText(context, resource.message ?: "Unknown error", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> Unit
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri.let {
            currentImageUri = it
            if (it != null) {
                viewModel.uploadProfileImage(it)
            }
        }
    }

    private fun actionMenu() {
        binding.apply {
            ivProfile.setOnClickListener {
                startGallery()
            }

            gantiPassword.setOnClickListener {
                showChangePasswordDialog()
            }
            logout.setOnClickListener {
                viewModel.logout()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            privasiProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_privacyFragment)
            }
            tntKamiProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
            }
        }
    }

    private fun showChangePasswordDialog() {
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password_bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        val oldPassword = bottomSheetView.findViewById<EditText>(R.id.etOldPassword)
        val newPassword = bottomSheetView.findViewById<EditText>(R.id.etNewPassword)
        val confirmNewPassword = bottomSheetView.findViewById<EditText>(R.id.etConfirmNewPassword)
        val btnChangePassword = bottomSheetView.findViewById<Button>(R.id.btnChangePassword)

        btnChangePassword.setOnClickListener {
            if (validatePasswordFields(oldPassword, newPassword, confirmNewPassword)) {
                viewModel.changePassword(oldPassword.text.toString(), newPassword.text.toString())
                bottomSheetDialog.dismiss()
            }
        }

        bottomSheetDialog.show()
    }

    private fun validatePasswordFields(oldPassword: EditText, newPassword: EditText, confirmNewPassword: EditText): Boolean {
        // Check if any field is empty
        if (oldPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Silahkan isi password sebelumnya", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Silahkan isi password yang baru", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmNewPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Silahkan isi konfirmasi password terlebih dahulu", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if new password meets the minimum length requirement
        val minPasswordLength = 6 // Adjust this based on your requirements
        if (newPassword.text.length < minPasswordLength) {
            Toast.makeText(context, "Password baru minimal $minPasswordLength karakter", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if new password and confirmation match
        if (newPassword.text.toString() != confirmNewPassword.text.toString()) {
            Toast.makeText(context, "Konfirmasi password tidak sama dengan password baru", Toast.LENGTH_SHORT).show()
            return false
        }

        // All validations passed
        return true
    }


    private fun userProfile() {
        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Success -> {
                        showLoading(false)
                        binding.profileName.text = "${it.data?.firstName} ${it.data?.lastName}"

                        val imagePath = it.data?.imagePath
                        if (imagePath.isNullOrEmpty()) {
                            // Jika imagePath kosong, set gambar default dengan border
                            Glide.with(requireContext())
                                .load(R.drawable.ic_default_profile)
                                .into(binding.profileImage)
                        } else {
                            // Jika imagePath tidak kosong, load gambar dengan border menggunakan Glide
                            Glide.with(requireContext())
                                .load(imagePath)
                                .into(binding.profileImage)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun getVersion() {
        val manager = requireActivity().packageManager
        val info =
            manager.getPackageInfo(requireActivity().packageName, PackageManager.GET_ACTIVITIES)
        binding.appVersion.text = "Versi ${info.versionName.toString()}"
    }

    private fun showRatingDialog() {
        val rateUsDialog = RateUsDialog(requireContext())
        rateUsDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparant)))
        rateUsDialog.setCancelable(false)
        rateUsDialog.show()
    }
}