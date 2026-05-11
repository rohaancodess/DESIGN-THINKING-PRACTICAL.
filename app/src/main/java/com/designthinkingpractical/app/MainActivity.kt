package com.designthinkingpractical.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Scaffold

import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.designthinkingpractical.app.data.worker.SyncWorker
import com.designthinkingpractical.app.di.Injection
import com.designthinkingpractical.app.ui.auth.AuthViewModel
import com.designthinkingpractical.app.ui.auth.LoginScreen
import com.designthinkingpractical.app.ui.auth.OtpVerificationScreen
import com.designthinkingpractical.app.ui.emergency.EmergencyViewModel
import com.designthinkingpractical.app.ui.home.HomeScreen
import com.designthinkingpractical.app.ui.theme.DesignThinkingPracticalAppTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val authRepository = Injection.provideAuthRepository(this)
        val emergencyRepository = Injection.provideEmergencyRepository(this)
        
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(authRepository) as T
                    modelClass.isAssignableFrom(EmergencyViewModel::class.java) -> EmergencyViewModel(emergencyRepository) as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

        // Schedule periodic sync
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueue(syncRequest)

        setContent {
            DesignThinkingPracticalAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
                    val emergencyViewModel: EmergencyViewModel = viewModel(factory = viewModelFactory)

                    
                    val startDestination = "splash"

                    Scaffold(
                        bottomBar = {
                            com.designthinkingpractical.app.ui.components.BottomNavBar(navController = navController)
                        }
                    ) { innerPadding ->

                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("splash") {
                                com.designthinkingpractical.app.ui.splash.SplashScreen(
                                    hasToken = authRepository.getToken() != null,
                                    onNavigateToHome = {
                                        navController.navigate("home") { popUpTo("splash") { inclusive = true } }
                                    },
                                    onNavigateToOnboarding = {
                                        navController.navigate("onboarding") { popUpTo("splash") { inclusive = true } }
                                    }
                                )
                            }
                            composable("onboarding") {
                                com.designthinkingpractical.app.ui.onboarding.OnboardingScreen(
                                    onFinishOnboarding = {
                                        navController.navigate("login") { popUpTo("onboarding") { inclusive = true } }
                                    }
                                )
                            }
                            composable("login") {
                                LoginScreen(
                                    viewModel = authViewModel,
                                    onNavigateToOtp = { email, isLogin ->
                                        navController.navigate("otp/$email/$isLogin")
                                    },
                                    onNavigateToHome = {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("otp/{email}/{isLogin}") { backStackEntry ->
                                val email = backStackEntry.arguments?.getString("email") ?: ""
                                
                                OtpVerificationScreen(
                                    viewModel = authViewModel,
                                    email = email,
                                    onVerified = {
                                        navController.navigate("login") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("home") {
                                HomeScreen(
                                    authRepository = authRepository,
                                    emergencyViewModel = emergencyViewModel,
                                    onNavigateToEmergency = { navController.navigate("emergency") },
                                    onNavigateToMap = { navController.navigate("map") },
                                    onNavigateToChat = { navController.navigate("chat/John Doe") },
                                    onNavigateToProfile = { navController.navigate("profile") }
                                )
                            }
                            composable("emergency") {
                                com.designthinkingpractical.app.ui.emergency.EmergencyRequestScreen(
                                    viewModel = emergencyViewModel,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                            composable("map") {
                                com.designthinkingpractical.app.ui.maps.DonorMapScreen(
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                            composable("community") {
                                com.designthinkingpractical.app.ui.community.CommunityScreen()
                            }
                            composable("chat/{donorName}") { backStackEntry ->
                                val donorName = backStackEntry.arguments?.getString("donorName") ?: "Donor"
                                com.designthinkingpractical.app.ui.chat.ChatScreen(
                                    donorName = donorName,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                            composable("bloodbank") {
                                com.designthinkingpractical.app.ui.bloodbank.BloodBankScreen(
                                    viewModel = emergencyViewModel,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable("profile") {
                                com.designthinkingpractical.app.ui.profile.ProfileScreen(
                                    onNavigateBack = { navController.popBackStack() },
                                    onLogout = {
                                        authRepository.logout()
                                        navController.navigate("login") { popUpTo(0) }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}