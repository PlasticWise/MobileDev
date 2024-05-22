package com.example.myintermediate.view

import androidx.lifecycle.ViewModel
import com.example.myintermediate.repository.AuthenticationRepository

class MapsViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {

    fun getUserMap () = authenticationRepository.getUserMap()
}