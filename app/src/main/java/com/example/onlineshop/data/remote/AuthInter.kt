package com.example.onlineshop.data.remote

import com.example.onlineshop.domain.model.AppSession
import okhttp3.Interceptor
import okhttp3.Response

class AuthInter(
    private val appSession: AppSession
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if(!appSession.isAuth)
            return chain.proceed(request)

        val newRequest = request
            .newBuilder()
            .addHeader("Authorization", "Bearer ${appSession.currentLogin?.token}")
            .build()

        return chain.proceed(newRequest)
    }


}