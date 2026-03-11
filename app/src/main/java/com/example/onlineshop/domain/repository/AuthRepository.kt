package com.example.onlineshop.domain.repository

import com.example.onlineshop.domain.model.LoginRequest
import com.example.onlineshop.domain.model.LoginResponse
import com.example.onlineshop.domain.model.RegisterRequest
import com.example.onlineshop.domain.model.User

interface AuthRepository {
    suspend fun loginAsync(loginRequest: LoginRequest): LoginResponse
    suspend fun registerAsync(registerRequest: RegisterRequest): LoginResponse
    suspend fun getProfileAsync(userId: String): User?
    suspend fun logoutAsync(): Result<Unit>
}