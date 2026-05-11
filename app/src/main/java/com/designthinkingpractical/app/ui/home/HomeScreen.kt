package com.designthinkingpractical.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.designthinkingpractical.app.domain.repository.AuthRepository

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.designthinkingpractical.app.ui.emergency.EmergencyViewModel


@Composable
fun HomeScreen(
    authRepository: AuthRepository,
    emergencyViewModel: EmergencyViewModel,
    onNavigateToEmergency: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val activeRequests by emergencyViewModel.activeRequests.collectAsState()
    val criticalCount = activeRequests.count { it.emergencyLevel == "Critical" }

    Scaffold(
        topBar = {
            HomeTopBar(onProfileClick = onNavigateToProfile)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToEmergency,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Emergency Request")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp)
        ) {
            item { GreetingHeader() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { EmergencyBanner(criticalCount, onNavigateToEmergency) }

            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { QuickActionGrid(onNavigateToMap, onNavigateToEmergency) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { AIPredictionSection() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onProfileClick: () -> Unit) {
    TopAppBar(
        title = { Text("BloodLink AI", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
        actions = {
            IconButton(onClick = { /* Notifications */ }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
            }
            IconButton(onClick = onProfileClick) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Composable
fun GreetingHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Hello, Hero!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("O+ | Bronze Donor | 3 Donations", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun EmergencyBanner(count: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🚨", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("$count Critical Requests Nearby", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Tap to view details and save a life.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}


@Composable
fun QuickActionGrid(onMapClick: () -> Unit, onEmergencyClick: () -> Unit) {
    Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ActionCard(title = "Request\nBlood", icon = Icons.Filled.Add, modifier = Modifier.weight(1f), onClick = onEmergencyClick)
        Spacer(modifier = Modifier.width(12.dp))
        ActionCard(title = "Nearby\nDonors", icon = Icons.Filled.LocationOn, modifier = Modifier.weight(1f), onClick = onMapClick)
        Spacer(modifier = Modifier.width(12.dp))
        ActionCard(title = "Blood\nBanks", icon = Icons.Filled.Favorite, modifier = Modifier.weight(1f), onClick = { /* TODO */ })
    }
}

@Composable
fun ActionCard(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.aspectRatio(1f).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun AIPredictionSection() {
    Text("AI Smart Insights", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("High Probability of Shortage", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text("Our AI predicts a shortage of O- blood in your area by next week due to upcoming weather conditions. Consider donating soon.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
        }
    }
}
