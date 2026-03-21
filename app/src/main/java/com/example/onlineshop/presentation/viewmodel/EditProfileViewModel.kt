package com.example.onlineshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.User
import com.example.onlineshop.domain.model.UserProfile
import com.example.onlineshop.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val session: AppSession
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val profile = repository.getUserProfile(session.userId)
                _userProfile.value = profile
                _photoUri.value = profile?.photoUrl
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Ошибка загрузки профиля"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(
        firstName: String,
        lastName: String,
        address: String,
        phone: String
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null
            try {
                val currentProfile = _userProfile.value
                if (currentProfile != null) {
                    val updatedProfile = currentProfile.copy(
                        firstName = firstName,
                        lastName = lastName,
                        address = address.ifBlank { null },
                        phone = phone.ifBlank { null },
                        photoUrl = _photoUri.value ?: currentProfile.photoUrl
                    )

                    repository.updateUserProfile(updatedProfile)

                    session.currentLogin?.let { loginResponse ->
                        val updatedUser = User(
                            id = loginResponse.user.id,
                            email = loginResponse.user.email,
                            firstName = firstName,
                            lastName = lastName,
                            photo = _photoUri.value ?: loginResponse.user.photo,
                            address = address.ifBlank { null },
                            phone = phone.ifBlank { null }
                        )
                        session.setSession(loginResponse.copy(user = updatedUser))
                    }

                    _userProfile.value = updatedProfile
                    _saveSuccess.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Ошибка сохранения: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun updatePhoto(uri: String) {
        _photoUri.value = uri
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }

    fun clearFields() {
        _userProfile.value = null
        _photoUri.value = null
    }
}