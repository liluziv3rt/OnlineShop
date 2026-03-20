package com.example.onlineshop.domain.usecase
import com.example.onlineshop.domain.repository.ProductRepository

import javax.inject.Inject

class ToggleFavouriteUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke(productId: String) = repo.toggleFavourite(productId)
}