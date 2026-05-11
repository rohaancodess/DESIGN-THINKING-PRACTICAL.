package com.designthinkingpractical.app.data.remote

import com.designthinkingpractical.app.domain.model.BloodBank
import com.designthinkingpractical.app.domain.model.EmergencyRequest
import retrofit2.http.*

interface EmergencyApi {
    @GET("api/emergency/active")
    suspend fun getActiveRequests(): List<EmergencyRequest>

    @POST("api/emergency")
    suspend fun createRequest(@Body request: Map<String, Any>): EmergencyRequest

    @GET("api/bloodbank")
    suspend fun getBloodBanks(): List<BloodBank>
}
