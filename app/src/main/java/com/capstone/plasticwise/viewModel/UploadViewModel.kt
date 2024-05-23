package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository
import java.io.File

class UploadViewModel(private val repository: AuthenticationRepository): ViewModel() {

    fun uploadImage(file: File, description: String, lat: Double, lon:Double) = repository.uploadImage(file, description, lat, lon)
}