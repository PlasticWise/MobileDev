package com.capstone.plasticwise.data.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.capstone.plasticwise.utils.Converters
import com.google.gson.annotations.SerializedName

@TypeConverters(Converters::class)
@Entity(tableName = "crafting_item")
data class ResponseCraftingItem(
	@SerializedName("createdAt")
	val createdAt: String,

	@SerializedName("equip")
	val equip: List<String>,

	@SerializedName("howto")
	val howto: List<String>,

	@SerializedName("imageUrl")
	val imageUrl: String,

	@PrimaryKey
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
