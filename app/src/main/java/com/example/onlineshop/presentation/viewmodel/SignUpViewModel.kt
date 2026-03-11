// presentation/viewmodel/SignUpViewModel.kt
package com.example.onlineshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.CustomResult
import com.example.onlineshop.domain.model.RegisterRequest
import com.example.onlineshop.domain.usecase.RegisterUseCase
import com.example.onlineshop.presentation.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Init)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    private val _registerRequest = MutableStateFlow(
        RegisterRequest(
            email = "",
            password = "",
            firstName = "",
            lastName = ""
        )
    )
    private val _confirmPassword = MutableStateFlow("")

    val registerRequest: StateFlow<RegisterRequest> = _registerRequest.asStateFlow()
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    fun updateFirstName(firstName: String) {
        _registerRequest.value = _registerRequest.value.copy(firstName = firstName)
    }

    fun updateLastName(lastName: String) {
        _registerRequest.value = _registerRequest.value.copy(lastName = lastName)
    }

    fun updateEmail(email: String) {
        _registerRequest.value = _registerRequest.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _registerRequest.value = _registerRequest.value.copy(password = password)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun validateForm(): Boolean {
        val request = _registerRequest.value
        return request.email.isNotBlank() &&
                request.password.isNotBlank() &&
                request.password.length >= 6 &&
                request.password == _confirmPassword.value
    }

    fun signUp() {
        if (!validateForm()) {
            _resultState.value = ResultState.Error("Please check your input")
            return
        }

        _resultState.value = ResultState.Loading

        viewModelScope.launch {
            when (val result = registerUseCase(_registerRequest.value)) {
                is CustomResult.Failure -> {
                    _resultState.value = ResultState.Error(result.message)
                }
                is CustomResult.Success -> {
                    _resultState.value = ResultState.Success("Registration successful! Please login.")
                }
            }
        }
    }

    fun resetState() {
        _resultState.value = ResultState.Init
    }
}