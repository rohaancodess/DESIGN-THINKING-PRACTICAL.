package com.designthinkingpractical.app.data.repository

import com.designthinkingpractical.app.data.local.TokenManager
import com.designthinkingpractical.app.data.remote.AuthApi
import com.designthinkingpractical.app.domain.model.AuthResponse
import com.designthinkingpractical.app.domain.model.MessageResponse
import com.designthinkingpractical.app.domain.model.User
import com.designthinkingpractical.app.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun sendOtp(email: String): Result<MessageResponse> {
        return try {
            val request = mapOf("email" to email)
            val response = api.sendOtp(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to send OTP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<MessageResponse> {
        return try {
            val request = mapOf("email" to email, "otp" to otp)
            val response = api.verifyOtp(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to verify OTP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val request = mapOf("name" to name, "email" to email, "password" to password)
            val response = api.register(request)
            if (response.isSuccessful && response.body() != null) {
                saveToken(response.body()!!.token)
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to register"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val request = mapOf("email" to email, "password" to password)
            val response = api.login(request)
            if (response.isSuccessful && response.body() != null) {
                saveToken(response.body()!!.token)
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to login"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<User> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("No token found"))
            val response = api.getProfile("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }

    override fun getToken(): String? {
        return tokenManager.getToken()
    }

    override fun logout() {
        tokenManager.clearToken()
    }
}
