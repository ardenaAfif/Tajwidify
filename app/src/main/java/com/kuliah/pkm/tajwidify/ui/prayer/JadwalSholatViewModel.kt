package com.kuliah.pkm.tajwidify.ui.prayer

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuliah.pkm.tajwidify.data.response.PrayerResponse
import com.kuliah.pkm.tajwidify.data.retrofit.ApiService
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JadwalSholatViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _prayerTimes = MutableLiveData<PrayerResponse>()
    val prayerTimes: LiveData<PrayerResponse> = _prayerTimes

    fun fetchPrayerTimes(idKota: String, tahun: String, bulan: String, tanggal: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getDailyPrayerTimes(idKota, tahun, bulan, tanggal)
                if (response.isSuccessful) {
                    _prayerTimes.postValue(response.body())
                } else {
                    Log.d("JadwalSholat", "Failed to retrieve data")
                }
            } catch (e: Exception) {
                Log.d("JadwalSholat", e.message.toString())
            }
        }
    }
}