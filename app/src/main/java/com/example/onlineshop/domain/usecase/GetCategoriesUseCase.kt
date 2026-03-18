package com.example.onlineshop.domain.usecase

import com.example.onlineshop.domain.repository.ProductRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke() = repo.getCategories()
}