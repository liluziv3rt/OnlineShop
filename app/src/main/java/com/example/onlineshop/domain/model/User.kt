package com.example.onlineshop.domain.model

data class User(
    val id: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val photo: String? = null,
    val address: String? = null,
    val phone: String? = null
)
