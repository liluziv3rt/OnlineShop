package com.example.onlineshop.domain.repository

import com.example.onlineshop.domain.model.LoginRequest
import com.example.onlineshop.domain.model.LoginResponse

interface AuthRepository {

    suspend fun loginAsync(loginRequest: LoginRequest) : LoginResponse

}