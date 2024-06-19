package com.capstone.plasticwise.data.remote

data class ResponsePosts(
	val createdAt: String,
	val imageUrl: String,
	val id: String,
	val categories: String,
	val title: String,
	val body: String,
	val authorId: String,
	val type: String,
	val updatedAt: String
)

