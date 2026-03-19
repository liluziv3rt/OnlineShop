package com.example.onlineshop.domain.model

import android.util.Log

class AppSession {

    private var _currentLogin : LoginResponse? = null

    val currentLogin : LoginResponse? get() {
        Log.d("AppSession", "currentLogin getter called, is null: ${_currentLogin == null}")
        return _currentLogin
    }

    val userId: String
        get() {
            val id = _currentLogin?.user?.id
            Log.d("AppSession", "userId getter called, id=$id")
            return requireNotNull(id) { "User not authorized" }
        }

    val isAuth: Boolean get() = _currentLogin != null

    fun setSession(loginResponse: LoginResponse){
        Log.d("AppSession", "setSession called with user: ${loginResponse.user.firstName} ${loginResponse.user.lastName}")
        _currentLogin = loginResponse
    }

    fun resetSession(){
        Log.d("AppSession", "resetSession called")
        _currentLogin = null
    }
}