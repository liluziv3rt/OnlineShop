package com.example.onlineshop.data.dto

data class ProductDto(
    val id: String,
    val title: String,
    val description: String,
    val cost: Double,
    val category_id: String?,
    val is_best_seller: Boolean?,
    val photo_url: String?
)