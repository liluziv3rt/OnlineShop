package com.example.onlineshop.data.dto

data class ProfileDto(
    val id: String,
    val created_at: String,
    val photo: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null
)
