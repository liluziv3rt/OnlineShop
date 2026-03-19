package com.example.onlineshop.data.repository

import com.example.onlineshop.data.dto.CartDto
import com.example.onlineshop.data.dto.FavouriteDto
import com.example.onlineshop.data.remote.ApiService
import com.example.onlineshop.domain.model.Action
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.Category
import com.example.onlineshop.domain.model.Product
import com.example.onlineshop.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ApiService,
    private val session: AppSession
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        val userId = session.userId
        // Если userId пустой (например, до логина), то списки избранного/корзины пустые
        val favs = if (userId.isNotBlank()) {
            try {
                api.getFavourites("eq.$userId")   // ← исправлено
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()

        val cart = if (userId.isNotBlank()) {
            try {
                api.getCart("eq.$userId")         // ← исправлено
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()

        val products = api.getProducts() // этот запрос уже работает (200)

        return products.map { dto ->
            Product(
                id = dto.id,
                title = dto.title,
                cost = dto.cost,
                description = dto.description,
                categoryId = dto.category_id,
                isBestSeller = dto.is_best_seller == true,
                imageUrl = dto.photo_url ?: "",
                isFavourite = favs.any { it.product_id == dto.id },
                inCart = cart.any { it.product_id == dto.id }
            )
        }.sortedByDescending { it.isBestSeller }
    }

    override suspend fun toggleFavourite(productId: String) {
        val userFilter = "eq.${session.userId}"
        val productFilter = "eq.$productId"   // добавляем оператор
        val favs = api.getFavourites(userFilter)
        if (favs.any { it.product_id == productId }) {
            api.removeFromFavourite(productFilter, userFilter)   // используем оба фильтра
        } else {
            api.addToFavourite(FavouriteDto(productId, session.userId))
        }
    }

    override suspend fun toggleCart(productId: String) {
        val userFilter = "eq.${session.userId}"
        val productFilter = "eq.$productId"   // добавляем оператор
        val cart = api.getCart(userFilter)
        if (cart.any { it.product_id == productId }) {
            api.removeFromCart(productFilter, userFilter)        // используем оба фильтра
        } else {
            api.addToCart(CartDto(productId, session.userId, 1))
        }
    }

    override suspend fun getCategories(): List<Category> {
        return api.getCategories().map {
            Category(it.id, it.title)
        }
    }

    override suspend fun getActions(): List<Action> {
        return api.getActions().map {
            Action(it.id, it.photo)
        }
    }
}