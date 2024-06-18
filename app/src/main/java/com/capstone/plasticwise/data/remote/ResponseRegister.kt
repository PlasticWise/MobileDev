package com.capstone.plasticwise.data.remote

import com.google.gson.annotations.SerializedName

data class ResponseRegister(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class RegisterRequest(
	@SerializedName("email")
	val email: String,

	@SerializedName("displayName")
	val displayName: String,

	@SerializedName("password")
	val password: String
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("displayName")
	val displayName: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
