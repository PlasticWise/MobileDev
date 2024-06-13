package com.capstone.plasticwise.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.data.remote.ListStoryItem
import com.capstone.plasticwise.repository.AuthenticationRepository

class HomeFragmentViewModel(private val repository: AuthenticationRepository) : ViewModel() {


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

//    fun getCraft() = repository.getCraft()

    fun getAllCrafting()  = repository.responseCrafting()
}