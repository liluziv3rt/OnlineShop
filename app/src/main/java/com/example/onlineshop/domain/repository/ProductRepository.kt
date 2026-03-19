package com.example.onlineshop.domain.repository

import com.example.onlineshop.domain.model.Action
import com.example.onlineshop.domain.model.Category
import com.example.onlineshop.domain.model.Product
import com.example.onlineshop.domain.model.UserProfile

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun toggleFavourite(productId: String)
    suspend fun toggleCart(productId: String)

    suspend fun getActions(): List<Action>
    suspend fun getCategories(): List<Category>

    suspend fun getUserProfile(userId: String): UserProfile?

    suspend fun updateUserProfile(profile: UserProfile)

}