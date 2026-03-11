package com.example.onlineshop.data.dto

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val options: RegisterOptionsDto = RegisterOptionsDto()
)

