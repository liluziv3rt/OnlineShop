package com.example.onlineshop.domain.utils

data class ParsedName(
    val firstName: String,
    val lastName: String
)

object NameParser {
    fun parseFullName(fullName: String): ParsedName {
        val parts = fullName.trim().split(" ")

        return when {
            parts.isEmpty() || fullName.isBlank() -> {
                throw IllegalArgumentException("Имя не может быть пустым")
            }
            parts.size == 1 -> {
                throw IllegalArgumentException("Пожалуйста, введите имя и фамилию через пробел")
            }
            else -> {
                ParsedName(
                    firstName = parts[0],
                    lastName = parts.drop(1).joinToString(" ") // Все остальное - фамилия
                )
            }
        }
    }
}
