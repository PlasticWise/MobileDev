package com.capstone.plasticwise.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.data.database.StoryDatabase
import com.capstone.plasticwise.data.database.StoryRemoteMediator
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.data.pref.UserPreference
import com.capstone.plasticwise.data.remote.ApiService
import com.capstone.plasticwise.data.remote.ListStoryItem
import com.capstone.plasticwise.data.remote.RegisterRequest
import com.capstone.plasticwise.data.remote.RegisterResponse
import com.capstone.plasticwise.data.remote.ResponseCraftingItem
import com.capstone.plasticwise.data.remote.ResponseLogin
import com.capstone.plasticwise.data.remote.ResponseStory
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

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseLogin::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    suspend fun responseCrafting(): List<ResponseCraftingItem> {
        val successResponse = apiService.getAllCrafting()
        storyDatabase.craftingDao().insertCraft(successResponse)
        return successResponse
    }

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getAllCraftingFromLocal(): List<ResponseCraftingItem> {
        return storyDatabase.craftingDao().getAllCraft()
    }
//    fun getPost() = liveData {
//        emit(Result.Loading)
//        try {
//            val successResponse = apiService.getStories()
//            val limitedResponse = successResponse.listStory.take(5)
//            emit(Result.Success(limitedResponse))
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
//            emit(Result.Error(errorResponse.message.toString()))
//        }
//    }

    fun getUserMap() = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getStoriesLocation(1)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getDetail(id: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getDetailStories(id)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getDetailCrafting(id: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getDetailCrafting(id)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }


//    fun getCraft() = liveData {
//        emit(Result.Loading)
//        try {
//            val successResponse = apiService.getStories()
//            val limitedResponse = successResponse.listStory.take(3)
//            emit(Result.Success(limitedResponse))
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
//            emit(Result.Error(errorResponse.message.toString()))
//        }
//    }
    fun register(email: String, displayName: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val emailRequestBody = email.toRequestBody("multipart/form-data".toMediaType())
            val displayNameRequestBody = displayName.toRequestBody("multipart/form-data".toMediaType())
            val passwordRequestBody = password.toRequestBody("multipart/form-data".toMediaType())
            val successResponse = apiService.register(emailRequestBody, displayNameRequestBody, passwordRequestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit((errorResponse.message.let { Result.Error(it) }))
        }
    }

    fun uploadImage(imageFile: File, description: String, lat: Double, lon: Double) =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val successResponse = apiService.uploadImage(imageMultipart, requestBody, lat, lon)
                emit(Result.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
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
