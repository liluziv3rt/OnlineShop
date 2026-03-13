package com.example.onlineshop.domain.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,  // Полное имя (Имя Фамилия)
    val consentGiven: Boolean  // Согласие на обработку данных
)


