package com.example.onlineshop.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.domain.model.AppSession
import com.example.onlineshop.domain.model.User
import com.example.onlineshop.domain.model.UserProfile
import com.example.onlineshop.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val session: AppSession
) : ViewModel() {

    private val _userProfile = MutableStateFlow(session.currentLogin?.user)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    init {
        Log.d("MenuViewModel", "init: user from session = ${session.currentLogin?.user?.firstName} ${session.currentLogin?.user?.lastName}")
        // Если в сессии нет, можно загрузить, но пока уберём
        // if (session.currentLogin == null) {
        //     loadUserProfile()
        // }
    }

    fun logout() {
        session.resetSession()
    }
}