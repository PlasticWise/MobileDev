package com.capstone.plasticwise.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin


    @Multipart
    @POST("register")
    suspend fun register(
        @Part("email") email: RequestBody,
        @Part("displayName") displayName: RequestBody,
        @Part("password") password: RequestBody
    ): ResponseRegister

    @GET("crafting")
    suspend fun getAllCrafting() : List<ResponseCraftingItem>

    @GET("crafting/{id}")
    suspend fun getDetailCrafting(
        @Path("id") id: String
    ): ResponseCraftingItem

    @GET("posts")
    suspend fun getAllPosts() : ResponsePostUser

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ResponseStory

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location: Int = 1
    ): ResponseStory

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String
    ): ResponseDetail

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double
    ): FileUploadResponse
}