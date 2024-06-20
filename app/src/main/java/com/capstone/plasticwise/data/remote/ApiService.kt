package com.capstone.plasticwise.data.remote

import retrofit2.http.DELETE
import com.capstone.plasticwise.model.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Multipart
    @POST("register")
    suspend fun register(
        @Part("email") email: RequestBody,
        @Part("displayName") displayName: RequestBody,
        @Part("password") password: RequestBody
    ): ResponseRegister

    @Multipart
    @POST("detection")
    suspend fun detection(
        @Part image: MultipartBody.Part
    ): ResponseDetection

    @Multipart
    @POST("posts")
    suspend fun uploadPost(
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part("authorId") authorId: RequestBody,
        @Part("categories") categories: RequestBody,
        @Part("type") type: RequestBody
    ): ResponsePosts

//    @Multipart
//    @POST("posts")
//    suspend fun updatePost(
//        @Part image: MultipartBody.Part,
//        @Part ("title") title: RequestBody,
//        @Part ("body") body : RequestBody,
//        @Part ("authorId") authorId : RequestBody,
//        @Part ("categories") categories : RequestBody,
//        @Part ("type") type : RequestBody
//    ) : ResponseUpdate

    @GET("posts/author/{uid}")
    suspend fun getAllPostsByAuthor(
        @Path("uid") uid: String
    ): List<ResponsePostUserItem>

    @GET("crafting")
    suspend fun getAllCrafting(): List<ResponseCraftingItem>

    @GET("crafting/{id}")
    suspend fun getDetailCrafting(
        @Path("id") id: String
    ): ResponseCraftingItem

    @Multipart
    @PATCH("posts/{id}")
    suspend fun updatePostUserById(
        @Path("id") id: String,
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part("categories") categories: RequestBody,
        @Part("type") type: RequestBody,
    ): ResponsePostUserItem

    @DELETE("posts/{id}")
    suspend fun deletePostUserById(
        @Path("id") id: String
    ): ResponsePostUserItem

    @GET("crafting/categories/{categories}")
    suspend fun getCraftingByCategories(
        @Path("categories") categories: String
    ): ResponseCraftingItem

    @GET("posts")
    suspend fun getAllPosts(): List<ResponsePostUserItem>


    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ResponseStory

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location: Int = 1
    ): ResponseStory

    @GET("posts/{id}")
    suspend fun getDetailPosts(
        @Path("id") id: String
    ): ResponsePostUserItem


    @Multipart
    @POST("posts")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part("categories") categories: RequestBody,
        @Part("type") type: RequestBody,
    ): FileUploadResponse
}