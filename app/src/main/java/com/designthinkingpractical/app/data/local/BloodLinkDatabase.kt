package com.designthinkingpractical.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "emergencies")
data class EmergencyEntity(
    @PrimaryKey val id: String,
    val patientName: String,
    val bloodGroup: String,
    val hospital: String,
    val unitsRequired: Int,
    val emergencyLevel: String,
    val contactNumber: String,
    val isSynced: Boolean = true
)


@Entity(tableName = "donors")
data class DonorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val bloodGroup: String,
    val latitude: Double,
    val longitude: Double
)

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM emergencies")
    fun getAllEmergencies(): Flow<List<EmergencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencies(emergencies: List<EmergencyEntity>)
    
    @Query("SELECT * FROM emergencies WHERE isSynced = 0")
    suspend fun getUnsyncedEmergencies(): List<EmergencyEntity>
}

@Dao
interface DonorDao {
    @Query("SELECT * FROM donors")
    fun getAllDonors(): Flow<List<DonorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonors(donors: List<DonorEntity>)
}

@Database(entities = [EmergencyEntity::class, DonorEntity::class], version = 1, exportSchema = false)
abstract class BloodLinkDatabase : RoomDatabase() {
    abstract fun emergencyDao(): EmergencyDao
    abstract fun donorDao(): DonorDao
}
