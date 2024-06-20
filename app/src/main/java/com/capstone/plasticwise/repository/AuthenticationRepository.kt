package com.capstone.plasticwise.repository

import androidx.lifecycle.liveData
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.data.database.StoryDatabase
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.data.pref.UserPreference
import com.capstone.plasticwise.data.remote.ApiService
import com.capstone.plasticwise.data.remote.FileUploadResponse
import com.capstone.plasticwise.data.remote.ResponseCraftingItem
import com.capstone.plasticwise.data.remote.ResponseDetection
import com.capstone.plasticwise.data.remote.ResponsePostUserItem
import com.capstone.plasticwise.data.remote.ResponsePosts
import com.capstone.plasticwise.data.remote.ResponseRegister
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class AuthenticationRepository private constructor(
    private var apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun responseCrafting(): List<ResponseCraftingItem> {
        val successResponse = apiService.getAllCrafting()
        storyDatabase.craftingDao().insertCraft(successResponse)
        return successResponse
    }



    fun getAllPostsByAuthor(uid: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getAllPostsByAuthor(uid)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponsePostUserItem::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    suspend fun getAllCraftingFromLocal(): List<ResponseCraftingItem> {
        return storyDatabase.craftingDao().getAllCraft()
    }

    fun getDetailPosts(id: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getDetailPosts(id)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponsePostUserItem::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    fun getDetailCrafting(id: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getDetailCrafting(id)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseCraftingItem::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    fun getDetailCraftingByCategories(categories: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getCraftingByCategories(categories)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseCraftingItem::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    fun getAllPost() = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getAllPosts()
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponsePostUserItem::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    fun register(email: String, displayName: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val emailRequestBody = email.toRequestBody("multipart/form-data".toMediaType())
            val displayNameRequestBody =
                displayName.toRequestBody("multipart/form-data".toMediaType())
            val passwordRequestBody = password.toRequestBody("multipart/form-data".toMediaType())
            val successResponse =
                apiService.register(emailRequestBody, displayNameRequestBody, passwordRequestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseRegister::class.java)
            emit((errorResponse.message.let { Result.Error(it) }))
        }
    }

    fun deletePostById(id: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.deletePostUserById(id)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponsePostUserItem::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    fun postUser(
        imageFile: File,
        title: String,
        body: String,
        authorId: String,
        categories: String,
        type: String
    ) = liveData {
        emit(Result.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestImageFile
        )
        try {
            val titleRequestBody = title.toRequestBody("multipart/form-data".toMediaType())
            val bodyRequestBody = body.toRequestBody("multipart/form-data".toMediaType())
            val authorIdRequestBody = authorId.toRequestBody("multipart/form-data".toMediaType())
            val categoriesRequestBody =
                categories.toRequestBody("multipart/form-data".toMediaType())
            val typeRequestBody = type.toRequestBody("multipart/form-data".toMediaType())
            val successResponse = apiService.uploadPost(
                imageMultipart,
                titleRequestBody,
                bodyRequestBody,
                authorIdRequestBody,
                categoriesRequestBody,
                typeRequestBody
            )
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponsePosts::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    fun updatePostUserById(id: String, imageFile: File?, title: String, body: String, type: String, categories: String) = liveData(Dispatchers.IO){
        emit(Result.Loading)
        val imageMultipart = imageFile?.let {
            val requestImageFile = it.asRequestBody("image/jpeg".toMediaType())
            MultipartBody.Part.createFormData("file", it.name, requestImageFile)
        }
        try {
            val titleRequestBody = title.toRequestBody("multipart/form-data".toMediaType())
            val bodyRequestBody = body.toRequestBody("multipart/form-data".toMediaType())
            val categoriesRequestBody =
                categories.toRequestBody("multipart/form-data".toMediaType())
            val typeRequestBody = type.toRequestBody("multipart/form-data".toMediaType())
            val successResponse = apiService.updatePostUserById(id, imageMultipart, titleRequestBody, bodyRequestBody, categoriesRequestBody, typeRequestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponsePostUserItem::class.java)
            emit(Result.Error(errorResponse.toString()))
        }
    }

    fun detection(imageFile: File) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image", // Nama bagian seperti yang digunakan di Postman
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.detection(imageMultipart)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseDetection::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(Result.Error("Terjadi kesalahan: ${e.message}"))
        }
    }


    fun uploadImage(
        imageFile: File,
        title: String,
        body: String,
        categories: String,
        type: String,
    ) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        val titleBody = title.toRequestBody("text/plain".toMediaType())
        val bodyBody = body.toRequestBody("text/plain".toMediaType())
        val categoriesBody = categories.toRequestBody("text/plain".toMediaType())
        val typeBody = type.toRequestBody("text/plain".toMediaType())

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        try {
            val successResponse = apiService.uploadImage(
                image = imagePart,
                title = titleBody,
                body = bodyBody,
                categories = categoriesBody,
                type = typeBody
            )
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }
    fun update(apiService: ApiService) {
        this.apiService = apiService
    }


    companion object {
        @Volatile
        private var instance: AuthenticationRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            storyDatabase: StoryDatabase
        ) =
            instance ?: synchronized(this) {
                instance ?: AuthenticationRepository(apiService, userPreference, storyDatabase)
            }.also { instance = it }
    }
}
