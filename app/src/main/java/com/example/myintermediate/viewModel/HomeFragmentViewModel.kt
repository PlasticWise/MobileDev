package com.example.myintermediate.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myintermediate.data.pref.UserModel
import com.example.myintermediate.data.remote.ListStoryItem
import com.example.myintermediate.repository.AuthenticationRepository

class HomeFragmentViewModel(private val repository: AuthenticationRepository) : ViewModel() {


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)
}