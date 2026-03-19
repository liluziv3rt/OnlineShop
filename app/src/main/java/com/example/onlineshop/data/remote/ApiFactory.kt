package com.example.onlineshop.data.remote

import android.util.Log
import com.example.onlineshop.domain.model.AppSession
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    private const val BASE_URL = "https://xyidpqfyhhuajzmbsayq.supabase.co/"
    private const val SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh5aWRwcWZ5aGh1YWp6bWJzYXlxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI2MTQ0MzgsImV4cCI6MjA4ODE5MDQzOH0.pNEGPoaiu1xGhON-I375CHGJQQNv_bk8zXOMbr8rCAA"

    fun create(appSession: AppSession): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInter(appSession))
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("apikey", SUPABASE_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build()
                Log.d("ApiFactory", "Adding apikey to ${request.url}")
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}