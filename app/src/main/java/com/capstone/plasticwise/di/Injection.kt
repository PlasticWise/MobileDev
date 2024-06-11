package com.capstone.plasticwise.di

import android.content.Context
import com.capstone.plasticwise.data.database.StoryDatabase
import com.capstone.plasticwise.data.pref.UserPreference
import com.capstone.plasticwise.data.pref.dataStore
import com.capstone.plasticwise.data.remote.ApiConfig
import com.capstone.plasticwise.repository.AuthenticationRepository
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