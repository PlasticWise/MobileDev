package com.example.myintermediate.viewModel

import androidx.lifecycle.ViewModel
import com.example.myintermediate.repository.AuthenticationRepository

class RegisterViewModel(private val repository: AuthenticationRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}