package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository

class DetailViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    fun getDetail(id: String) = repository.getDetailPosts(id)

}