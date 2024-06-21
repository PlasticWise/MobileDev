package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository
import java.io.File

class DetectViewModel(val repository: AuthenticationRepository): ViewModel() {

    fun detection(image: File) = repository.detection(image)


}