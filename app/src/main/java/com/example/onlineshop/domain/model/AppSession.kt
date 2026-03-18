package com.example.onlineshop.domain.model

class AppSession {

    private var _currentLogin : LoginResponse? = null

    val currentLogin : LoginResponse? get() = _currentLogin

    val userId: String
        get() = requireNotNull(_currentLogin?.user?.id) {
            "User not authorized"
        }
    val isAuth: Boolean get() = _currentLogin != null

    fun setSession(loginResponse: LoginResponse){
        _currentLogin = loginResponse
    }

    fun resetSession(){
        _currentLogin = null
    }
}