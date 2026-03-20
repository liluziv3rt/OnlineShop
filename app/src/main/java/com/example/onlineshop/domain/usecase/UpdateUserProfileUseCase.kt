package com.example.onlineshop.domain.usecase
import com.example.onlineshop.domain.model.UserProfile
import com.example.onlineshop.domain.repository.ProductRepository

import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke(profile: UserProfile) = repo.updateUserProfile(profile)
}