package com.example.onlineshop.data.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    val email: String,
    val password: String,
    @SerializedName("data")
    val userData: UserMetadataDto
)

