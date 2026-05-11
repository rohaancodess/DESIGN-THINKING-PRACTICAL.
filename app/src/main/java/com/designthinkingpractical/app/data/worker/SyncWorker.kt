package com.designthinkingpractical.app.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.util.Log

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "Starting offline data synchronization...")
        
        return try {
            val repository = com.designthinkingpractical.app.di.Injection.provideEmergencyRepository(applicationContext)
            val result = repository.syncRequests()
            
            if (result.isSuccess) {
                Log.d("SyncWorker", "Synchronization complete.")
                Result.success()
            } else {
                Log.e("SyncWorker", "Sync failed: ${result.exceptionOrNull()?.message}")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync failed: ${e.message}")
            Result.retry()
        }
    }

}
