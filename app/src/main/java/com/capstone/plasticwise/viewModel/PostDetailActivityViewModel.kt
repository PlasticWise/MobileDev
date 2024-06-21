package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository

class PostDetailActivityViewModel(val repository: AuthenticationRepository) : ViewModel() {

    fun deletePostById(id: String) = repository.deletePostById(id)
}