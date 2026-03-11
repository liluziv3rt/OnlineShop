package com.example.onlineshop.domain.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

