package com.example.onlineshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.UserProfile
import com.example.onlineshop.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val session: AppSession
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Сначала пробуем взять из сессии
        session.currentLogin?.user?.let { user ->
            _userProfile.value = UserProfile(
                id = user.id,
                firstName = user.firstName ?: "",
                lastName = user.lastName ?: "",
                photoUrl = user.photo,
                address = user.address ?: "Не указано",
                phone = user.phone ?: "Не указан"
            )
        } ?: run {
            // Если в сессии нет, загружаем из БД
            loadUserProfile()
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Сначала пробуем взять из сессии
                session.currentLogin?.user?.let { user ->
                    _userProfile.value = UserProfile(
                        id = user.id,
                        firstName = user.firstName ?: "",
                        lastName = user.lastName ?: "",
                        photoUrl = user.photo,
                        address = user.address ?: "Не указано",
                        phone = user.phone ?: "Не указан"
                    )
                } ?: run {
                    // Если в сессии нет, загружаем из БД
                    val profile = repository.getUserProfile(session.userId)
                    _userProfile.value = profile
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _userProfile.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}