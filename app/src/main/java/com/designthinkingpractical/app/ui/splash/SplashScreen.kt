package com.designthinkingpractical.app.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    hasToken: Boolean,
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)
        if (hasToken) {
            onNavigateToHome()
        } else {
            onNavigateToOnboarding()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFC62828), Color(0xFF8E0000))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp).offset(y = (-50).dp)
        ) {
            // Heartbeat/Blood drop placeholder. In reality, a Lottie animation goes here.
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White.copy(alpha = alphaAnim.value), shape = androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                 Text("🩸", fontSize = 64.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "BloodLink AI",
                color = Color.White.copy(alpha = alphaAnim.value),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sharing Blood. Saving lives.",
                color = Color.White.copy(alpha = alphaAnim.value * 0.8f),
                fontSize = 16.sp,
            )
        }
    }
}
