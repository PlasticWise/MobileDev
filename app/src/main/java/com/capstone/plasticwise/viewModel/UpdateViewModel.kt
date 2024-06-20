package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository
import java.io.File

class UpdateViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    fun updateStory(
        id: String,
        fileImage: File,
        title: String,
        body: String,
        categories: String,
        type: String
    ) = repository.updatePostUserById(id, fileImage, title, body, type, categories)

    fun uploadStory(
        fileImage: File,
        authorId: String,
        title: String,
        body: String,
        categories: String,
        type: String
    ) = repository.postUser(fileImage, title, body, authorId, categories, type)
//    fun updateStory(
//        fileImage: File,
//        title: String,
//        body: String,
//        authorId: String,
//        categories: String,
//        type: String
//    ) = repository.updateUser(fileImage, title, body, authorId, categories, type)
}

