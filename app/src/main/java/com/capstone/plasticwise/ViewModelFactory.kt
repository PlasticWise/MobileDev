package com.capstone.plasticwise

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.plasticwise.di.Injection
import com.capstone.plasticwise.repository.AuthenticationRepository
import com.capstone.plasticwise.viewModel.CraftViewModel
import com.capstone.plasticwise.viewModel.DetailViewModel
import com.capstone.plasticwise.viewModel.DetectViewModel
import com.capstone.plasticwise.viewModel.HomeFragmentViewModel
import com.capstone.plasticwise.viewModel.HomeViewModel
import com.capstone.plasticwise.viewModel.LoginViewModel
import com.capstone.plasticwise.viewModel.PostDetailActivityViewModel
import com.capstone.plasticwise.viewModel.PostViewModel
import com.capstone.plasticwise.viewModel.ProfileFragmentViewModel
import com.capstone.plasticwise.viewModel.RegisterViewModel
import com.capstone.plasticwise.viewModel.SplashScreenViewModel
import com.capstone.plasticwise.viewModel.UpdateViewModel
import com.capstone.plasticwise.viewModel.UploadViewModel

class ViewModelFactory(private val repository: AuthenticationRepository, private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java) -> {
                HomeFragmentViewModel(repository, context) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java) -> {
                ProfileFragmentViewModel(repository) as T
            }
            
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> {
                SplashScreenViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CraftViewModel::class.java) -> {
                CraftViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetectViewModel::class.java) -> {
                DetectViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdateViewModel::class.java) -> {
                UpdateViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PostDetailActivityViewModel::class.java) -> {
                PostDetailActivityViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    context.applicationContext
                ).also { INSTANCE = it }
            }
        }
    }
}
