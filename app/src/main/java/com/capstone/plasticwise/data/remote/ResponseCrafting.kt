package com.capstone.plasticwise.data.remote

import com.google.gson.annotations.SerializedName

data class ResponseCraftingItem(
	@SerializedName("createdAt")
	val createdAt: String,

	@SerializedName("equip")
	val equip: List<String>,

	@SerializedName("howto")
	val howto: List<String>,

	@SerializedName("imageUrl")
	val imageUrl: String,

	@SerializedName("id")
	val id: String,

	@SerializedName("categories")
	val categories: String,

	@SerializedName("title")
	val title: String,

	@SerializedName("authorId")
	val authorId: String,

	@SerializedName("type")
	val type: String,

	@SerializedName("tools")
	val tools: List<String>,

	@SerializedName("updatedAt")
	val updatedAt: String
)
