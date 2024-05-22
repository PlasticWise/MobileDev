package com.example.myintermediate.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myintermediate.data.pref.UserModel
import com.example.myintermediate.repository.AuthenticationRepository

class ProfileFragmentViewModel(private val repository: AuthenticationRepository): ViewModel() {

    fun getSession():LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}