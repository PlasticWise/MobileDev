package com.example.myintermediate.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myintermediate.data.pref.UserModel
import com.example.myintermediate.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AuthenticationRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}