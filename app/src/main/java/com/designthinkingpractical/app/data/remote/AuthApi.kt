package com.designthinkingpractical.app.data.remote

import com.designthinkingpractical.app.domain.model.AuthResponse
import com.designthinkingpractical.app.domain.model.MessageResponse
import com.designthinkingpractical.app.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/send-otp")
    suspend fun sendOtp(@Body request: Map<String, String>): Response<MessageResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body request: Map<String, String>): Response<MessageResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: Map<String, String>): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: Map<String, String>): Response<AuthResponse>

    @GET("api/user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<User>
}
