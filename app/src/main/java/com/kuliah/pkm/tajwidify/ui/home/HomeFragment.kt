package com.kuliah.pkm.tajwidify.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.adapter.MateriAdapter
import com.kuliah.pkm.tajwidify.data.response.Jadwal
import com.kuliah.pkm.tajwidify.databinding.FragmentHomeBinding
import com.kuliah.pkm.tajwidify.ui.prayer.JadwalSholatViewModel
import com.kuliah.pkm.tajwidify.ui.profile.ProfileViewModel
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val userViewModel by viewModels<ProfileViewModel>()

    private val materiViewModel by viewModels<HomeViewModel>()
    private lateinit var materiAdapter: MateriAdapter
    private val jadwalSholatViewModel: JadwalSholatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRvMateri()
        materiSetup()
        getUser()
        onQuizCompleted()

        jadwalSholatAction()
        jadwalSholatViewModel.fetchPrayerTimesForToday("1621")
        observeNextJadwalSholat()
    }

    private fun observeNextJadwalSholat() {
        jadwalSholatViewModel.prayerTimes.observe(viewLifecycleOwner) {prayerResponse ->
            prayerResponse?.data?.jadwal?.let { jadwal ->
                updatePrayerTimeUi(jadwal)
            }
        }
    }

    private fun updatePrayerTimeUi(jadwal: Jadwal) {
        val currentTime = Calendar.getInstance()

        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val now = formatter.format(currentTime.time)

        val nextPrayerTime = determineNextPrayer(jadwal, now)

        binding.apply {
            prayerName.text = nextPrayerTime.first
            prayerTime.text = nextPrayerTime.second
            updateCurrentDate(jadwal.date)
        }
    }

    private fun determineNextPrayer(jadwal: Jadwal, currentTime: String): Pair<String, String> {
        return when {
            currentTime < jadwal.subuh -> "Waktu Subuh" to jadwal.subuh
            currentTime < jadwal.dzuhur -> "Waktu Dhuhur" to jadwal.dzuhur
            currentTime < jadwal.ashar -> "Waktu Ashar" to jadwal.ashar
            currentTime < jadwal.maghrib -> "Waktu Maghrib" to jadwal.maghrib
            else -> "Waktu Isya" to jadwal.isya
        }
    }

    private fun updateCurrentDate(apiDate: String) {
        // Format asli dari API (misalnya "yyyy-MM-dd")
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Format target yang diinginkan untuk ditampilkan (misalnya "EEEE, dd MMMM yyyy")
        val targetFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

        try {
            val date = originalFormat.parse(apiDate)
            val formattedDate = targetFormat.format(date ?: Date())
            binding.prayerDate.text = formattedDate
        } catch (e: ParseException) {
            e.printStackTrace()
            binding.prayerDate.text = apiDate // Atau tampilkan pesan error/tanggal default
        }
    }

    private fun jadwalSholatAction() {
        binding.apply {
            cardPrayer.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_jadwalSholatFragment)
            }
        }
    }

    private fun onQuizCompleted() {
        materiViewModel.refreshMateriList()
    }

    private fun setupRvMateri() {
        materiAdapter = MateriAdapter(requireContext())
        binding.rvMateri.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = materiAdapter
        }
    }

    private fun materiSetup() {
        lifecycleScope.launchWhenStarted {
            materiViewModel.modulList.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        hideLoading()
                        materiAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e("Materi List", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getUser() {
        lifecycleScope.launchWhenStarted {
            userViewModel.user.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        binding.firstName.text = it.data?.firstName

                        val imagePath = it.data?.imagePath
                        if (imagePath.isNullOrEmpty()) {
                            Glide.with(requireContext())
                                .load(R.drawable.ic_default_profile)
                                .into(binding.ivProfile)
                        } else {
                            Glide.with(requireContext())
                                .load(imagePath)
                                .into(binding.ivProfile)
                        }
                    }
                    is Resource.Error -> {
                        Log.e("TAG", it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            shimmerFrame.visibility = View.VISIBLE
            shimmerFrame.startShimmer()
        }
    }

    private fun hideLoading() {
        binding.apply{
            shimmerFrame.stopShimmer()
            shimmerFrame.visibility = View.GONE
        }
    }
}