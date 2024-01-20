package com.kuliah.pkm.tajwidify.ui.prayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kuliah.pkm.tajwidify.databinding.FragmentJadwalSholatBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class JadwalSholatFragment : Fragment() {

    private lateinit var binding: FragmentJadwalSholatBinding
    private val viewModel: JadwalSholatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJadwalSholatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        viewModel.fetchPrayerTimesForToday("1621")
        updateUiJadwalSholat()
    }

    private fun updateUiJadwalSholat() {
        viewModel.prayerTimes.observe(viewLifecycleOwner) { response ->
            hideLoading()
            binding.apply {
                btnBack.setOnClickListener {
                    findNavController().navigateUp()
                }

                tvDaerahJadwal.text = response.data.lokasi
                tvTanggalJadwal.text = formatToIndonesianDate(response.data.jadwal.tanggal)

                tvWaktuSholatSubuh.text = response.data.jadwal.subuh
                tvWaktuSholatDhuhur.text = response.data.jadwal.dzuhur
                tvWaktuSholatAsar.text = response.data.jadwal.ashar
                tvWaktuSholatMaghrib.text = response.data.jadwal.maghrib
                tvWaktuSholatIsya.text = response.data.jadwal.isya
            }
        }
    }

    private fun formatToIndonesianDate(dateString: String): String {
        val originalFormat = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale("id", "ID"))
        val targetFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return try {
            val date = originalFormat.parse(dateString)
            val formattedDate = targetFormat.format(date ?: Date())
            Log.d("JadwalSholat", "Formatted Date: $formattedDate") // Check the formatted date
            formattedDate
        } catch (e: ParseException) {
            Log.e("JadwalSholat", "Date parsing error", e)
            dateString // Return the original string in case of parsing error
        }
    }

    private fun showLoading() {
        binding.apply {
            progressIndicator.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressIndicator.visibility = View.GONE
        }
    }
}