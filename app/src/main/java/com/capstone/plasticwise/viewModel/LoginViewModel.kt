package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) = repository.login(email, password)
}