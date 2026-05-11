package com.designthinkingpractical.app.domain.repository

import com.designthinkingpractical.app.domain.model.BloodBank
import com.designthinkingpractical.app.domain.model.EmergencyRequest
import kotlinx.coroutines.flow.Flow

interface EmergencyRepository {
    fun getActiveRequests(): Flow<List<EmergencyRequest>>
    suspend fun createRequest(
        patientName: String,
        bloodGroup: String,
        hospital: String,
        unitsRequired: Int,
        emergencyLevel: String,
        contactNumber: String
    ): Result<EmergencyRequest>
    
    suspend fun getBloodBanks(): Result<List<BloodBank>>
    suspend fun syncRequests(): Result<Unit>
}
