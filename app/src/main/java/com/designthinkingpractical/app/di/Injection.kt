package com.designthinkingpractical.app.di

import android.content.Context
import androidx.room.Room
import com.designthinkingpractical.app.data.local.BloodLinkDatabase
import com.designthinkingpractical.app.data.local.TokenManager
import com.designthinkingpractical.app.data.remote.AuthApi
import com.designthinkingpractical.app.data.remote.EmergencyApi
import com.designthinkingpractical.app.data.repository.AuthRepositoryImpl
import com.designthinkingpractical.app.data.repository.EmergencyRepositoryImpl
import com.designthinkingpractical.app.domain.repository.AuthRepository
import com.designthinkingpractical.app.domain.repository.EmergencyRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {
    private var database: BloodLinkDatabase? = null
    private var retrofit: Retrofit? = null

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun getDatabase(context: Context): BloodLinkDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                BloodLinkDatabase::class.java,
                "bloodlink.db"
            ).fallbackToDestructiveMigration().build()
        }
        return database!!
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val api = getRetrofit().create(AuthApi::class.java)
        val tokenManager = TokenManager(context)
        return AuthRepositoryImpl(api, tokenManager)
    }

    fun provideEmergencyRepository(context: Context): EmergencyRepository {
        val api = getRetrofit().create(EmergencyApi::class.java)
        val dao = getDatabase(context).emergencyDao()
        return EmergencyRepositoryImpl(api, dao)
    }
}
