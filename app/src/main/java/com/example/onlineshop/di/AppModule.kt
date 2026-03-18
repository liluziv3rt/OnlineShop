package com.example.onlineshop.di

import com.example.onlineshop.data.remote.ApiFactory
import com.example.onlineshop.data.remote.ApiService
import com.example.onlineshop.data.repository.AuthRepositoryImpl
import com.example.onlineshop.data.repository.ProductRepositoryImpl
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.repository.AuthRepository
import com.example.onlineshop.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(appSession: AppSession): ApiService {
        return ApiFactory.create(appSession)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        appSession: AppSession
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, appSession)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        apiService: ApiService,
        appSession: AppSession
    ): ProductRepository {
        return ProductRepositoryImpl(apiService, appSession)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppSession(): AppSession {
        return AppSession()
    }
}

