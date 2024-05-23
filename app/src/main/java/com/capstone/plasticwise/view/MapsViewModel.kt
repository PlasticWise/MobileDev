package com.capstone.plasticwise.view

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository

class MapsViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {

    fun getUserMap () = authenticationRepository.getUserMap()
}