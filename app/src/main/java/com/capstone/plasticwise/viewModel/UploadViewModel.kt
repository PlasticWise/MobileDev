package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository
import java.io.File

class UploadViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    fun uploadImage(file: File, title: String, body: String, categories: String, type: String) =
        repository.uploadImage(file, title, body, categories, type)
}
