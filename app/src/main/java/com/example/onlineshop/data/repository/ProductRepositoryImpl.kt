package com.example.onlineshop.data.repository

import android.util.Log
import com.example.onlineshop.data.dto.CartDto
import com.example.onlineshop.data.dto.FavouriteDto
import com.example.onlineshop.data.dto.UpdateProfileDto
import com.example.onlineshop.data.remote.ApiService
import com.example.onlineshop.domain.model.Action
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.Category
import com.example.onlineshop.domain.model.Product
import com.example.onlineshop.domain.model.UserProfile
import com.example.onlineshop.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ApiService,
    private val session: AppSession
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        val userId = session.userId

        val favs = if (userId.isNotBlank()) {
            try {
                api.getFavourites("eq.$userId")
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()

        val cart = if (userId.isNotBlank()) {
            try {
                api.getCart("eq.$userId")
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()

        val products = api.getProducts()

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
        val productFilter = "eq.$productId"
        val favs = api.getFavourites(userFilter)
        if (favs.any { it.product_id == productId }) {
            api.removeFromFavourite(productFilter, userFilter)
        } else {
            api.addToFavourite(FavouriteDto(productId, session.userId))
        }
    }

    override suspend fun toggleCart(productId: String) {
        val userFilter = "eq.${session.userId}"
        val productFilter = "eq.$productId"
        val cart = api.getCart(userFilter)
        if (cart.any { it.product_id == productId }) {
            api.removeFromCart(productFilter, userFilter)
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

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val filter = "eq.$userId"
            Log.d("ProductRepository", "Loading profile with filter: $filter")
            val token = session.currentLogin?.accessToken
            val response = api.getProfileAsync("Bearer $token", filter)
            Log.d("ProductRepository", "Profile response size: ${response.size}")
            if (response.isNotEmpty()) {
                val profile = response.first()
                UserProfile(
                    id = profile.id,
                    firstName = profile.firstname ?: "",
                    lastName = profile.lastname ?: "",
                    photoUrl = profile.photo
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error loading profile", e)
            null
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        try {
            val filter = "eq.${profile.id}"
            val token = session.currentLogin?.accessToken ?: throw Exception("No token")

            val updateDto = UpdateProfileDto(
                firstname = profile.firstName ?: "",
                lastname = profile.lastName ?: "",
                address = profile.address,
                phone = profile.phone,
                photo = profile.photoUrl
            )

            val response = api.updateProfileAsync("Bearer $token", filter, updateDto)

            if (!response.isSuccessful) {
                throw Exception("Failed to update profile: ${response.code()}")
            }

            Log.d("ProductRepository", "Profile updated successfully")
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error updating profile", e)
            throw e
        }
    }


}