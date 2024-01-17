package com.kuliah.pkm.tajwidify.data.retrofit

import com.kuliah.pkm.tajwidify.data.response.LocationPrayerResponse
import com.kuliah.pkm.tajwidify.data.response.PrayerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("sholat/kota/cari/{keyword}")
    suspend fun searchCity(@Path("keyword") keyword: String): Response<LocationPrayerResponse>

    @GET("sholat/jadwal/{idkota}/{tahun}/{bulan}/{tanggal}")
    suspend fun getDailyPrayerTimes(
        @Path("idkota") idKota: String,
        @Path("tahun") tahun: String,
        @Path("bulan") bulan: String,
        @Path("tanggal") tanggal: String
    ): Response<PrayerResponse>
}