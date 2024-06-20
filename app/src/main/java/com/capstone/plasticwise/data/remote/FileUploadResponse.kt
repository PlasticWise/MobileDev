package com.capstone.plasticwise.data.remote

data class FileUploadResponse(
    val success: Boolean,
    val message: String,
    val data: UploadedImageData? = null
)

data class UploadedImageData(
    val id: String,
    val imageUrl: String,
    val title: String,
    val body: String,
    val categories: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String
)
