package com.example.myintermediate.di

import android.content.Context
import com.example.myintermediate.data.database.StoryDatabase
import com.example.myintermediate.data.pref.UserPreference
import com.example.myintermediate.data.pref.dataStore
import com.example.myintermediate.data.remote.ApiConfig
import com.example.myintermediate.repository.AuthenticationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): AuthenticationRepository {
        val pRef = UserPreference.getInstance(context.dataStore)
        val user = runBlocking {
            pRef.getSession().first()
        }
        val storyDatabase = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(user.token)
        return AuthenticationRepository.getInstance(apiService, pRef, storyDatabase)
    }
}