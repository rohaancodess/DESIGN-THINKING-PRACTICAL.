package com.designthinkingpractical.app.domain.repository

import com.designthinkingpractical.app.domain.model.AuthResponse
import com.designthinkingpractical.app.domain.model.MessageResponse
import com.designthinkingpractical.app.domain.model.User

interface AuthRepository {
    suspend fun sendOtp(email: String): Result<MessageResponse>
    suspend fun verifyOtp(email: String, otp: String): Result<MessageResponse>
    suspend fun register(name: String, email: String, password: String): Result<AuthResponse>
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun getProfile(): Result<User>
    fun saveToken(token: String)
    fun getToken(): String?
    fun logout()
}
