package com.example.onlineshop.data.dto

data class UserDto(
    val id: String,
    val email: String,
    val created_at: String,
    val user_metadata: UserMetadataDto? = null
)

