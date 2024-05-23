package com.capstone.plasticwise.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.repository.AuthenticationRepository
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