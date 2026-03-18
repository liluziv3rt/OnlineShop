// data/remote/ApiService.kt
package com.example.onlineshop.data.remote

import com.example.onlineshop.data.dto.ActionDto
import com.example.onlineshop.data.dto.CartDto
import com.example.onlineshop.data.dto.CategoryDto
import com.example.onlineshop.data.dto.FavouriteDto
import com.example.onlineshop.data.dto.LoginRequestDto
import com.example.onlineshop.data.dto.LoginResponseDto
import com.example.onlineshop.data.dto.ProductDto
import com.example.onlineshop.data.dto.ProfileDto
import com.example.onlineshop.data.dto.RegisterRequestDto
import com.example.onlineshop.data.dto.UserDto
import retrofit2.http.*

interface ApiService {

    @POST("auth/v1/token?grant_type=password")
    suspend fun loginAsync(@Body loginRequestDto: LoginRequestDto): LoginResponseDto

    @POST("auth/v1/signup")
    suspend fun registerAsync(@Body registerRequestDto: RegisterRequestDto): LoginResponseDto

    @POST("auth/v1/logout")
    suspend fun logoutAsync(@Header("Authorization") authorization: String)

    @GET("rest/v1/profiles")
    suspend fun getProfileAsync(
        @Header("Authorization") authorization: String,
        @Query("id") filter: String   // теперь передаём "eq.uuid"
    ): List<ProfileDto>

    @GET("auth/v1/user")
    suspend fun getUserAsync(@Header("Authorization") authorization: String): UserDto

    // ---------- Товары, категории, акции (публичные) ----------
    @GET("rest/v1/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("rest/v1/products")
    suspend fun getProductsByCategory(@Query("category_id") filter: String): List<ProductDto>


    @GET("rest/v1/products")
    suspend fun searchProducts(@Query("title") query: String): List<ProductDto>

    @GET("rest/v1/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("rest/v1/actions")
    suspend fun getActions(): List<ActionDto>

    // ---------- Избранное (требуется авторизация) ----------
    @GET("rest/v1/favourite")
    suspend fun getFavourites(@Query("user_id") userId: String): List<FavouriteDto>

    @POST("rest/v1/favourite")
    suspend fun addToFavourite(@Body dto: FavouriteDto)

    @DELETE("rest/v1/favourite")
    suspend fun removeFromFavourite(
        @Query("product_id") productId: String,
        @Query("user_id") userId: String
    )

    // ---------- Корзина (требуется авторизация) ----------
    @GET("rest/v1/cart")
    suspend fun getCart(@Query("user_id") userId: String): List<CartDto>

    @POST("rest/v1/cart")
    suspend fun addToCart(@Body dto: CartDto)

    @DELETE("rest/v1/cart")
    suspend fun removeFromCart(
        @Query("product_id") productId: String,
        @Query("user_id") userId: String
    )
}



