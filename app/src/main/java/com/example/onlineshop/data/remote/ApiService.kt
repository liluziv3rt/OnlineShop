// data/remote/ApiService.kt
package com.example.onlineshop.data.remote

import com.example.onlineshop.data.dto.LoginRequestDto
import com.example.onlineshop.data.dto.LoginResponseDto
import com.example.onlineshop.data.dto.ProfileDto
import com.example.onlineshop.data.dto.RegisterRequestDto
import com.example.onlineshop.data.dto.UserDto
import retrofit2.http.*

interface ApiService {

    @POST("auth/v1/token?grant_type=password")
    suspend fun loginAsync(
        @Body loginRequestDto: LoginRequestDto
    ): LoginResponseDto

    @POST("auth/v1/signup")
    suspend fun registerAsync(
        @Body registerRequestDto: RegisterRequestDto
    ): LoginResponseDto

    @POST("auth/v1/logout")
    suspend fun logoutAsync(
        @Header("Authorization") authorization: String
    ): Unit

    @GET("rest/v1/profiles")
    suspend fun getProfileAsync(
        @Header("Authorization") authorization: String,
        @Query("id") userId: String
    ): List<ProfileDto>

    @GET("auth/v1/user")
    suspend fun getUserAsync(
        @Header("Authorization") authorization: String
    ): UserDto
}