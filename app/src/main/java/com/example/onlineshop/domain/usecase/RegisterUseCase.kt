package com.example.onlineshop.domain.usecase

import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.CustomResult
import com.example.onlineshop.domain.model.RegisterRequest
import com.example.onlineshop.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val appSession: AppSession
) {

    suspend operator fun invoke(registerRequest: RegisterRequest): CustomResult<Unit> {
        return try {
            val response = authRepository.registerAsync(registerRequest)
            appSession.setSession(response)
            CustomResult.Success(Unit)
        } catch (ex: Exception) {
            CustomResult.Failure(ex.message ?: "Registration failed")
        }
    }
}
