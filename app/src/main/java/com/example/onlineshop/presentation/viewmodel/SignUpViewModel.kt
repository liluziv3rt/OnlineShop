package com.example.onlineshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.CustomResult
import com.example.onlineshop.domain.model.RegisterRequest
import com.example.onlineshop.domain.usecase.RegisterUseCase
import com.example.onlineshop.domain.utils.NameParser
import com.example.onlineshop.presentation.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Init)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _fullName = MutableStateFlow("")
    private val _consentGiven = MutableStateFlow(false)

    val email: StateFlow<String> = _email.asStateFlow()
    val password: StateFlow<String> = _password.asStateFlow()
    val fullName: StateFlow<String> = _fullName.asStateFlow()
    val consentGiven: StateFlow<Boolean> = _consentGiven.asStateFlow()

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun updateFullName(fullName: String) {
        _fullName.value = fullName
    }

    fun updateConsent(consent: Boolean) {
        _consentGiven.value = consent
    }

    fun validateForm(): String? {
        return when {
            _email.value.isBlank() -> "Введите email"
            !Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", _email.value) ->
                "Некорректный email"
            _password.value.length < 6 -> "Пароль должен быть не менее 6 символов"
            _fullName.value.isBlank() -> "Введите имя и фамилию"
            !_consentGiven.value -> "Необходимо дать согласие на обработку данных"
            else -> {
                try {
                    NameParser.parseFullName(_fullName.value)
                    null
                } catch (e: IllegalArgumentException) {
                    e.message
                }
            }
        }
    }

    fun signUp() {
        val error = validateForm()
        if (error != null) {
            _resultState.value = ResultState.Error(error)
            return
        }

        _resultState.value = ResultState.Loading

        viewModelScope.launch {
            val registerRequest = RegisterRequest(
                email = _email.value,
                password = _password.value,
                fullName = _fullName.value,
                consentGiven = _consentGiven.value
            )

            when (val result = registerUseCase(registerRequest)) {
                is CustomResult.Failure -> {
                    _resultState.value = ResultState.Error(result.message)
                }
                is CustomResult.Success -> {
                    _resultState.value = ResultState.Success("Регистрация успешна! Пожалуйста, войдите.")
                }
            }
        }
    }

    fun resetState() {
        _resultState.value = ResultState.Init
    }
}