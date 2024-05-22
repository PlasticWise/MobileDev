package com.example.myintermediate.viewModel

import androidx.lifecycle.ViewModel
import com.example.myintermediate.repository.AuthenticationRepository

class DetailViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    fun getDetail(id: String) = repository.getDetail(id)

}