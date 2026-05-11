package com.designthinkingpractical.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designthinkingpractical.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class OtpSent(val email: String) : AuthState()
    object OtpVerified : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun sendOtp(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.sendOtp(email).fold(
                onSuccess = {
                    _authState.value = AuthState.OtpSent(email)
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Failed to send OTP")
                }
            )
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.verifyOtp(email, otp).fold(
                onSuccess = {
                    _authState.value = AuthState.OtpVerified
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Invalid OTP")
                }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.login(email, password).fold(
                onSuccess = {
                    _authState.value = AuthState.Success
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Login failed")
                }
            )
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.register(name, email, password).fold(
                onSuccess = {
                    _authState.value = AuthState.Success
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Registration failed")
                }
            )
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
