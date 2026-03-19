package com.example.onlineshop.data.dto

import com.google.gson.annotations.SerializedName
data class ProfileDto(
    val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("created_at") val createdAt: String,
    val photo: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null
)
