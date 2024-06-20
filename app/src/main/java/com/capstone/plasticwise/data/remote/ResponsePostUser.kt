package com.capstone.plasticwise.data.remote

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity("post_user")
data class ResponsePostUserItem(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("imageUrl")
    val imageUrl: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("categories")
    val categories: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("body")
    val body: String,

    @field:SerializedName("authorId")
    val authorId: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)
