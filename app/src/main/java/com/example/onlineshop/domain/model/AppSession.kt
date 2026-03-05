package com.example.onlineshop.domain.model

class AppSession {

    private var _currentLogin : LoginResponse? = null

    val currentLogin : LoginResponse? get() = _currentLogin

    val isAuth: Boolean get() = _currentLogin != null

    fun setSession(loginResponse: LoginResponse){
        _currentLogin = loginResponse
    }

    fun resetSession(){
        _currentLogin = null
    }
}