// data/repository/AuthRepositoryImpl.kt
package com.example.onlineshop.data.repository

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

    override suspend fun registerAsync(registerRequest: RegisterRequest): LoginResponse {
        val requestDto = RegisterRequestDto(
            email = registerRequest.email,
            password = registerRequest.password,
            options = RegisterOptionsDto(
                data = UserMetadataDto(
                    firstname = registerRequest.firstName,
                    lastname = registerRequest.lastName
                )
            )
        )

        return try {
            val response = apiService.registerAsync(requestDto)

            LoginResponse(
                accessToken = "",
                refreshToken = "",
                user = User(
                    id = response.user.id,
                    email = response.user.email,
                    firstName = registerRequest.firstName,
                    lastName = registerRequest.lastName
                )
            )
        } catch (e: Exception) {
            throw Exception("Registration failed: ${e.message}")
        }
    }

    override suspend fun getProfileAsync(userId: String): User? {
        return try {
            val token = appSession.currentLogin?.accessToken ?: return null
            val profiles = apiService.getProfileAsync("Bearer $token", userId)
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
        val profile = try {
            val profiles = apiService.getProfileAsync("Bearer $token", userId)
            profiles.firstOrNull()
        } catch (e: Exception) {
            null
        }

        return User(
            id = userId,
            email = "", // email будет добавлен позже
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