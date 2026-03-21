package com.example.onlineshop.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.Action
import com.example.onlineshop.domain.model.Category
import com.example.onlineshop.domain.model.Product
import com.example.onlineshop.domain.model.UserProfile
import com.example.onlineshop.domain.repository.ProductRepository
import com.example.onlineshop.domain.usecase.GetCategoriesUseCase
import com.example.onlineshop.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val repo: ProductRepository
) : ViewModel() {

    var products = mutableStateListOf<Product>()
        private set

    var showAllProducts by mutableStateOf(false)
        private set

    var userProfile by mutableStateOf<UserProfile?>(null)

    var showAllActions by mutableStateOf(false)
        private set

    var categories = mutableStateListOf<Category>()
        private set

    var actions = mutableStateListOf<Action>()
        private set

    var selectedCategoryId by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadAllData()

    }

    private fun loadAllData() {
        load()
        loadCategories()
        loadActions()
    }

    fun toggleProductsShowAll() {
        showAllProducts = !showAllProducts
    }

    fun toggleActionsShowAll() {
        showAllActions = !showAllActions
    }

    fun toggleShowAllProducts() {
        showAllProducts = !showAllProducts
    }

    fun load() {
        viewModelScope.launch {
            try {
                val result = getProducts()
                products.clear()
                products.addAll(result)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Ошибка загрузки товаров: ${e.message}"
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val result = getCategoriesUseCase()
                categories.clear()
                categories.add(Category("", "Все"))
                categories.addAll(result)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Ошибка загрузки категорий: ${e.message}"
            }
        }
    }

    fun loadActions() {
        viewModelScope.launch {
            try {
                val result = repo.getActions()
                actions.clear()
                actions.addAll(result)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Ошибка загрузки акций: ${e.message}"
            }
        }
    }

    fun toggleFav(id: String) {
        viewModelScope.launch {
            try {
                repo.toggleFavourite(id)
                load()
            } catch (e: Exception) {
                errorMessage = "Ошибка при изменении избранного: ${e.message}"
            }
        }
    }

    fun toggleCart(id: String) {
        viewModelScope.launch {
            try {
                repo.toggleCart(id)
                load()
            } catch (e: Exception) {
                errorMessage = "Ошибка при изменении корзины: ${e.message}"
            }
        }
    }

    fun selectCategory(id: String?) {
        selectedCategoryId = id
        viewModelScope.launch {
            try {
                val all = getProducts()
                products.clear()
                products.addAll(
                    if (id.isNullOrEmpty()) all
                    else all.filter { it.categoryId == id }
                )
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Ошибка фильтрации: ${e.message}"
            }
        }
    }
}