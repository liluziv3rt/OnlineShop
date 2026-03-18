package com.example.onlineshop.data.dto

data class CartDto(
    val product_id: String,
    val user_id: String,
    val count: Int = 1
)