package com.example.myintermediate.viewModel

import androidx.lifecycle.ViewModel
import com.example.myintermediate.repository.AuthenticationRepository
import java.io.File

class UploadViewModel(private val repository: AuthenticationRepository): ViewModel() {

    fun uploadImage(file: File, description: String, lat: Double, lon:Double) = repository.uploadImage(file, description, lat, lon)
}