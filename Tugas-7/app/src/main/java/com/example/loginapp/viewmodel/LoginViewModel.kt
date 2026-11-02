package com.example.loginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginResult {
    data object Idle : LoginResult
    data object Loading : LoginResult
    data class Success(val username: String) : LoginResult
    data class Error(val message: String) : LoginResult
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val result: LoginResult = LoginResult.Idle
)

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { repository.seedDemoUser() }
    }

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value, result = LoginResult.Idle)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, result = LoginResult.Idle)
    }

    fun login() {
        val current = _uiState.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _uiState.value = current.copy(result = LoginResult.Error("Username dan password wajib diisi."))
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(result = LoginResult.Loading)
            val isValid = repository.login(current.username, current.password)
            _uiState.value = if (isValid) {
                current.copy(result = LoginResult.Success(current.username.trim()))
            } else {
                current.copy(result = LoginResult.Error("Username atau password salah. Coba lagi."))
            }
        }
    }

    fun logout() {
        _uiState.value = _uiState.value.copy(result = LoginResult.Idle, password = "")
    }
}
