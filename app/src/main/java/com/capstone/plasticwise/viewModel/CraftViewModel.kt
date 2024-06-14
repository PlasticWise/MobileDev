package com.capstone.plasticwise.viewModel

import androidx.lifecycle.ViewModel
import com.capstone.plasticwise.repository.AuthenticationRepository

class CraftViewModel(val repository: AuthenticationRepository): ViewModel() {
    fun getDetailCraft(id: String) = repository.getDetailCrafting(id)

    fun getDetailCraftByCategories(categories: String) = repository.getDetailCraftingByCategories(categories)
}