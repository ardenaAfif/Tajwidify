package com.kuliah.pkm.tajwidify.data.response

import com.google.gson.annotations.SerializedName

data class LocationPrayerResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: Boolean
)

data class DataItem(

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("id")
	val id: String
)
