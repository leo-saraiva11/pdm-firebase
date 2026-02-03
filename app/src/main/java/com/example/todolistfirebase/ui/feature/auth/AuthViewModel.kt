package com.example.todolistfirebase.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistfirebase.data.AuthRepository
import com.example.todolistfirebase.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<Result<Unit>?>(null)
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Result.Loading
            _authState.value = repository.login(email, password)
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Result.Loading
            _authState.value = repository.signUp(email, password)
        }
    }

    fun resetState() {
        _authState.value = null
    }
}
