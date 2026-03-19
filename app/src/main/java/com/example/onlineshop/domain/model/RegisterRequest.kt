package com.example.onlineshop.domain.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val consentGiven: Boolean
)


