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

        jadwalSholat()
        jadwalSholatViewModel.fetchPrayerTimesForToday("1621")
        observeNextJadwalSholat()
    }

    private fun observeNextJadwalSholat() {
        jadwalSholatViewModel.prayerTimes.observe(viewLifecycleOwner) {prayerResponse ->
            val nextPlayerTime = getNextPlayerTime(prayerResponse.data.jadwal)
            updatePrayerTimeUi(nextPlayerTime)

            updateCurrentDate(prayerResponse.data.jadwal.tanggal)
        }
    }

    private fun getNextPlayerTime(jadwal: Jadwal): Pair<String, String> {
        val currentTime = Calendar.getInstance()

        val formatter = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        val times = listOf(
            "Subuh" to formatter.parse(jadwal.subuh),
            "Dhuhur" to formatter.parse(jadwal.dzuhur),
            "Asar" to formatter.parse(jadwal.ashar),
            "Maghrib" to formatter.parse(jadwal.maghrib),
            "Isya" to formatter.parse(jadwal.isya)
        )

        for ((name, time) in times) {
            if (time != null && time.after(currentTime.time)) {
                return name to formatter.format(time)
            }
        }

        // If no future time is found (e.g., late night), return the first prayer time of the next day
        return "Asar" to formatter.format(formatter.parse(jadwal.ashar))
    }

    private fun updatePrayerTimeUi(nextPrayerTime: Pair<String, String>) {
        binding.prayerName.text = "Waktu ${nextPrayerTime.first}"
        binding.prayerTime.text = nextPrayerTime.second
    }

    private fun updateCurrentDate(apiDate: String) {
        // Assuming apiDate is in the format "EEEE, dd/MM/yyyy"
        val originalFormat = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale("id", "ID"))
        val targetFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val date = originalFormat.parse(apiDate)
        val formattedDate = targetFormat.format(date ?: Date())

        binding.prayerDate.text = formattedDate
    }

    private fun jadwalSholat() {
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