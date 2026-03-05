package com.example.onlineshop.data.repository

import com.example.onlineshop.data.mapper.LoginMapper
import com.example.onlineshop.data.remote.ApiService
import com.example.onlineshop.domain.model.LoginRequest
import com.example.onlineshop.domain.model.LoginResponse
import com.example.onlineshop.domain.repository.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {



    override suspend fun loginAsync(loginRequest: LoginRequest): LoginResponse {
        try {
            val response = apiService.loginAsync(LoginMapper.toLoginRequestDto(loginRequest))

            return LoginMapper.toLoginResponseDomain(response)

        }
        catch (ex: HttpException){

            if(ex.code() == 400){
                throw Exception("Неправильный логин или пароль")
            }

            throw ex
        }
    }


}