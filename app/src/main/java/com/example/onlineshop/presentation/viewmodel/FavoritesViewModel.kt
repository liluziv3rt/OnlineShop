package com.example.onlineshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.Product
import com.example.onlineshop.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val session: AppSession
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allProducts = repository.getProducts()
                _favorites.value = allProducts.filter { it.isFavourite }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFav(productId: String) {
        viewModelScope.launch {
            try {
                repository.toggleFavourite(productId)
                // Обновляем список после изменения
                loadFavorites()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleCart(productId: String) {
        viewModelScope.launch {
            try {
                repository.toggleCart(productId)
                // Обновляем статус корзины в текущем списке
                _favorites.value = _favorites.value.map {
                    if (it.id == productId) {
                        it.copy(inCart = !it.inCart)
                    } else it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}