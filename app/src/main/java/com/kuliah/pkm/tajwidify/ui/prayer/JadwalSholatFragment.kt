package com.kuliah.pkm.tajwidify.ui.prayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kuliah.pkm.tajwidify.R
import com.kuliah.pkm.tajwidify.databinding.FragmentJadwalSholatBinding
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.fetchPrayerTimes("1621", "2024", "01", "18")

        viewModel.prayerTimes.observe(viewLifecycleOwner) {response ->
            binding.apply {
                tvDaerahJadwal.text = response.data.lokasi
                tvTanggalJadwal.text = response.data.jadwal.tanggal

                tvWaktuSholatSubuh.text = response.data.jadwal.subuh
                tvWaktuSholatDhuhur.text = response.data.jadwal.dzuhur
                tvWaktuSholatAsar.text = response.data.jadwal.ashar
                tvWaktuSholatMaghrib.text = response.data.jadwal.maghrib
                tvWaktuSholatIsya.text = response.data.jadwal.isya
            }
        }
    }

}