package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository

class SettingsViewModel(val repository: AuthenticationRepository): ViewModel() {

    suspend fun getAllPostsByAuthor(uid: String) = repository.getAllPostsByAuthor(uid)
}