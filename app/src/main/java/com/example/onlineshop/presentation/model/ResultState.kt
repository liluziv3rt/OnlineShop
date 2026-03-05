package com.example.onlineshop.presentation.model

sealed class ResultState {

    data object Loading : ResultState()

    data object Init : ResultState()

    data class Success(val message: String = "") : ResultState()

    data class Error(val message: String) : ResultState()
}