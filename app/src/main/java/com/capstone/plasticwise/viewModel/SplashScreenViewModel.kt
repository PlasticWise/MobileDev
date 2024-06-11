package com.capstone.plasticwise.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.repository.AuthenticationRepository

class SplashScreenViewModel(val repository: AuthenticationRepository): ViewModel() {

    fun getSession() : LiveData<UserModel> {
       return repository.getSession().asLiveData()
    }
}