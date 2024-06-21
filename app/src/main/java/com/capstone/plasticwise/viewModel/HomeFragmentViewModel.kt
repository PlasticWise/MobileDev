package com.capstone.plasticwise.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.data.remote.ResponseCraftingItem
import com.capstone.plasticwise.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val repository: AuthenticationRepository, private val context: Context) : ViewModel() {

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable

    init {
        checkNetworkConnection()
    }

    private fun checkNetworkConnection() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return
        _isNetworkAvailable.value = when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val _allCrafting = MutableLiveData<Result<List<ResponseCraftingItem>>>()
    val allCrafting: LiveData<Result<List<ResponseCraftingItem>>> get() = _allCrafting

    fun getAllCrafting() {
        viewModelScope.launch {
            _allCrafting.postValue(Result.Loading)
            if (_isNetworkAvailable.value == true) {
                try {
                    val successResponse = repository.responseCrafting()
                    _allCrafting.postValue(Result.Success(successResponse))
                } catch (e: Exception) {
                    _allCrafting.postValue(Result.Error(e.message.toString()))
                }
            } else {
                try {
                    val localData = repository.getAllCraftingFromLocal()
                    _allCrafting.postValue(Result.Success(localData))
                } catch (e: Exception) {
                    _allCrafting.postValue(Result.Error(e.message.toString()))
                }
            }
        }
    }
}
