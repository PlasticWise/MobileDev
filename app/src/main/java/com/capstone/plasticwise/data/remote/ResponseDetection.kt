package com.capstone.plasticwise.data.remote

import com.google.gson.annotations.SerializedName

data class ResponseDetection(

	@field:SerializedName("data")
	val data: DetectionData,

	@field:SerializedName("confidence")
	val confidence: Double,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DetectionData(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("message")
	val message: String
)
