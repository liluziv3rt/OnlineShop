// domain/usecase/RegisterUseCase.kt
package com.example.onlineshop.domain.usecase

import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.CustomResult
import com.example.onlineshop.domain.model.RegisterRequest
import com.example.onlineshop.domain.repository.AuthRepository
import com.example.onlineshop.domain.utils.NameParser
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val appSession: AppSession
) {

    suspend operator fun invoke(registerRequest: RegisterRequest): CustomResult<Unit> {
        return try {
            // Разбираем имя на firstname и lastname
            val parsedName = NameParser.parseFullName(registerRequest.fullName)

            // Здесь можно сохранить факт согласия, если нужно
            // Например, в отдельную таблицу или в метаданные

            val response = authRepository.registerAsync(
                email = registerRequest.email,
                password = registerRequest.password,
                firstName = parsedName.firstName,
                lastName = parsedName.lastName
            )

            appSession.setSession(response)
            CustomResult.Success(Unit)
        } catch (ex: Exception) {
            CustomResult.Failure(ex.message ?: "Registration failed")
        }
    }
}