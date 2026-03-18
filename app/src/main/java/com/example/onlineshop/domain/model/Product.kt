package com.example.onlineshop.domain.model

data class Product(
    val id: String,
    val title: String,
    val cost: Double,
    val description: String,
    val categoryId: String?,
    val isBestSeller: Boolean,
    val imageUrl: String,
    val isFavourite: Boolean = false,
    val inCart: Boolean = false
)