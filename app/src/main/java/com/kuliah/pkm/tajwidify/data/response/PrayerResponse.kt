package com.kuliah.pkm.tajwidify.data.response

import com.google.gson.annotations.SerializedName

data class PrayerResponse(

	@field:SerializedName("request")
	val request: Request,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: Boolean
)

data class Request(

	@field:SerializedName("path")
	val path: String
)

data class Data(

	@field:SerializedName("jadwal")
	val jadwal: Jadwal,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("daerah")
	val daerah: String,

	@field:SerializedName("id")
	val id: Int
)

data class Jadwal(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("imsak")
	val imsak: String,

	@field:SerializedName("isya")
	val isya: String,

	@field:SerializedName("dzuhur")
	val dzuhur: String,

	@field:SerializedName("subuh")
	val subuh: String,

	@field:SerializedName("dhuha")
	val dhuha: String,

	@field:SerializedName("terbit")
	val terbit: String,

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("ashar")
	val ashar: String,

	@field:SerializedName("maghrib")
	val maghrib: String
)
