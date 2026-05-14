package com.designthinkingpractical.app.data.repository

import com.designthinkingpractical.app.data.local.EmergencyDao
import com.designthinkingpractical.app.data.local.EmergencyEntity
import com.designthinkingpractical.app.data.remote.EmergencyApi
import com.designthinkingpractical.app.domain.model.BloodBank
import com.designthinkingpractical.app.domain.model.EmergencyRequest
import com.designthinkingpractical.app.domain.repository.EmergencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class EmergencyRepositoryImpl(
    private val api: EmergencyApi,
    private val dao: EmergencyDao
) : EmergencyRepository {

    override fun getActiveRequests(): Flow<List<EmergencyRequest>> {
        return dao.getAllEmergencies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createRequest(
        patientName: String,
        bloodGroup: String,
        hospital: String,
        unitsRequired: Int,
        emergencyLevel: String,
        contactNumber: String
    ): Result<EmergencyRequest> {
        return try {
            val requestBody = mapOf(
                "patientName" to patientName,
                "bloodGroup" to bloodGroup,
                "hospital" to hospital,
                "unitsRequired" to unitsRequired,
                "emergencyLevel" to emergencyLevel,
                "contactNumber" to contactNumber
            )
            
            // Try remote first
            val remoteRequest = api.createRequest(requestBody)
            
            // Save to local
            dao.insertEmergencies(listOf(remoteRequest.toEntity(isSynced = true)))
            
            Result.success(remoteRequest)
        } catch (e: Exception) {
            // Offline support: Save locally with isSynced = false
            val localId = UUID.randomUUID().toString()
            val localRequest = EmergencyEntity(
                id = localId,
                patientName = patientName,
                bloodGroup = bloodGroup,
                hospital = hospital,
                unitsRequired = unitsRequired,
                emergencyLevel = emergencyLevel,
                contactNumber = contactNumber,
                isSynced = false
            )
            dao.insertEmergencies(listOf(localRequest))
            
            // Return a domain object based on local data
            Result.success(localRequest.toDomain())
        }
    }

    override suspend fun getBloodBanks(): Result<List<BloodBank>> {
        return try {
            val bloodBanks = api.getBloodBanks()
            Result.success(bloodBanks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncRequests(): Result<Unit> {
        return try {
            val unsynced = dao.getUnsyncedEmergencies()
            unsynced.forEach { entity ->
                val requestBody = mapOf(
                    "patientName" to entity.patientName,
                    "bloodGroup" to entity.bloodGroup,
                    "hospital" to entity.hospital,
                    "unitsRequired" to entity.unitsRequired,
                    "emergencyLevel" to entity.emergencyLevel,
                    "contactNumber" to entity.contactNumber
                )
                val remoteRequest = api.createRequest(requestBody)
                // Remove local unsynced and replace with remote
                dao.deleteEmergencyById(entity.id)
                dao.insertEmergencies(listOf(remoteRequest.toEntity(isSynced = true)))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Helper extensions
    private fun EmergencyEntity.toDomain() = EmergencyRequest(
        id = id,
        patientName = patientName,
        bloodGroup = bloodGroup,
        hospital = hospital,
        unitsRequired = unitsRequired,
        emergencyLevel = emergencyLevel,
        contactNumber = contactNumber,
        status = "Active", // Default for local
        createdAt = "" // Not stored locally in this simple version
    )

    private fun EmergencyRequest.toEntity(isSynced: Boolean) = EmergencyEntity(
        id = id,
        patientName = patientName,
        bloodGroup = bloodGroup,
        hospital = hospital,
        unitsRequired = unitsRequired,
        emergencyLevel = emergencyLevel,
        contactNumber = contactNumber,
        isSynced = isSynced
    )
}
