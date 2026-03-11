package com.example.onlineshop.data.dto

data class LoginResponseDto(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    val user: UserDto
)

