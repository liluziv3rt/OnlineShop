package com.example.onlineshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.CustomResult
import com.example.onlineshop.domain.model.LoginRequest
import com.example.onlineshop.domain.usecase.LoginUseCase
import com.example.onlineshop.presentation.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Init)

    val resultState : StateFlow<ResultState> = _resultState.asStateFlow()

    private val _loginRequest = MutableStateFlow(LoginRequest("", ""))

    val loginRequest : StateFlow<LoginRequest> = _loginRequest.asStateFlow()


    fun updateLoginRequest(loginRequest: LoginRequest){
        _loginRequest.value = loginRequest
    }

    fun signIn(){

        _resultState.value = ResultState.Loading

        viewModelScope.launch {

            when(val result = loginUseCase(_loginRequest.value)){
                is CustomResult.Failure -> {
                    _resultState.value = ResultState.Error(result.message)
                }
                is CustomResult.Success<Unit> -> {
                    _resultState.value = ResultState.Success()
                }
            }

        }

    }
}