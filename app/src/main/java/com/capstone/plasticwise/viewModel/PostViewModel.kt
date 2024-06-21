package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository

class PostViewModel(val repository: AuthenticationRepository): ViewModel() {

    fun getAllPost() = repository.getAllPost()
}