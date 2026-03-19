// data/repository/AuthRepositoryImpl.kt
package com.example.onlineshop.data.repository

import android.util.Log
import com.example.onlineshop.data.dto.LoginRequestDto
import com.example.onlineshop.data.dto.LoginResponseDto
import com.example.onlineshop.data.dto.RegisterOptionsDto
import com.example.onlineshop.data.dto.RegisterRequestDto
import com.example.onlineshop.data.dto.UserMetadataDto
import com.example.onlineshop.data.remote.ApiService
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.LoginRequest
import com.example.onlineshop.domain.model.LoginResponse
import com.example.onlineshop.domain.model.RegisterRequest
import com.example.onlineshop.domain.model.User
import com.example.onlineshop.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appSession: AppSession
) : AuthRepository {

    override suspend fun loginAsync(loginRequest: LoginRequest): LoginResponse {
        val requestDto = LoginRequestDto(
            email = loginRequest.email,
            password = loginRequest.password
        )

        return try {
            val response = apiService.loginAsync(requestDto)
            val user = getUserWithProfile(response.user.id, response.access_token)
            mapToDomain(response, user)
        } catch (e: Exception) {
            throw Exception("Login failed: ${e.message}")
        }
    }

    // data/repository/AuthRepositoryImpl.kt
    override suspend fun registerAsync(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): LoginResponse {
        val requestDto = RegisterRequestDto(
            email = email,
            password = password,
            userData = UserMetadataDto(
                firstname = firstName,
                lastname = lastName
            )
        )

        Log.d("AuthRepository", "Sending registration data: $requestDto")

        return try {
            val response = apiService.registerAsync(requestDto)
            Log.d("AuthRepository", "Registration response: $response")

            LoginResponse(
                accessToken = response.access_token,
                refreshToken = response.refresh_token,
                user = User(
                    id = response.user.id,
                    email = response.user.email,
                    firstName = firstName,
                    lastName = lastName
                )
            )
        } catch (e: Exception) {
            Log.e("AuthRepository", "Registration failed", e)
            throw Exception("Registration failed: ${e.message}")
        }
    }
    override suspend fun getProfileAsync(userId: String): User? {
        return try {
            val token = appSession.currentLogin?.accessToken ?: return null
            val filter = "eq.$userId"
            val profiles = apiService.getProfileAsync("Bearer $token", filter)
            profiles.firstOrNull()?.let { profile ->
                User(
                    id = profile.id,
                    email = "", // email не хранится в profiles
                    firstName = profile.firstname,
                    lastName = profile.lastname,
                    photo = profile.photo,
                    address = profile.address,
                    phone = profile.phone
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun logoutAsync(): Result<Unit> {
        return try {
            val token = appSession.currentLogin?.accessToken
            if (token != null) {
                apiService.logoutAsync("Bearer $token")
            }
            appSession.resetSession()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getUserWithProfile(userId: String, token: String): User {
        val filter = "eq.$userId"
        Log.d("AuthRepo", "getUserWithProfile: filter=$filter")

        val profile = try {
            val profiles = apiService.getProfileAsync("Bearer $token", filter)
            profiles.firstOrNull()
        } catch (e: Exception) {
            Log.e("AuthRepo", "Error loading profile", e)
            null
        }

        return User(
            id = userId,
            email = "",
            firstName = profile?.firstname,
            lastName = profile?.lastname,
            photo = profile?.photo,
            address = profile?.address,
            phone = profile?.phone
        )
    }
    private fun mapToDomain(dto: LoginResponseDto, user: User): LoginResponse {
        return LoginResponse(
            accessToken = dto.access_token,
            refreshToken = dto.refresh_token,
            user = user.copy(email = dto.user.email)
        )
    }
}