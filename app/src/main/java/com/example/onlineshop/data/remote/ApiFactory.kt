package com.example.onlineshop.data.remote

import com.example.onlineshop.domain.model.AppSession
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private const val BASE_URL = "http://127.0.0.1:8090/api/"

    fun create(appSession: AppSession) : ApiService {

        val client = OkHttpClient
            .Builder()
            .addInterceptor(AuthInter(appSession))
            .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        return retrofit.create(ApiService::class.java)
    }

}