package com.designthinkingpractical.app.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorMapScreen(onNavigateBack: () -> Unit) {
    var selectedFilter by remember { mutableStateOf("All") }
    val bloodGroups = listOf("All", "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")

    val donors = listOf(
        DonorInfo("John Doe", "O+", "2.1 km", "Available", 95),
        DonorInfo("Jane Smith", "A-", "4.5 km", "Available", 88),
        DonorInfo("Rahul Kumar", "B+", "1.3 km", "Available", 92),
        DonorInfo("Priya Sharma", "O+", "6.0 km", "Last donated 2 months ago", 78),
        DonorInfo("Amit Singh", "AB+", "3.2 km", "Available", 85),
    )

    val filteredDonors = if (selectedFilter == "All") donors else donors.filter { it.bloodGroup == selectedFilter }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Donors") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Live tracking banner
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Live Donor Tracking Active", fontWeight = FontWeight.Bold)
                        Text("${filteredDonors.size} donors found nearby", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                bloodGroups.take(5).forEach { group ->
                    FilterChip(
                        onClick = { selectedFilter = group },
                        label = { Text(group, fontSize = 12.sp) },
                        selected = selectedFilter == group
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Donor list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredDonors.size) { index ->
                    DonorCard(filteredDonors[index])
                }
            }
        }
    }
}

@Composable
fun DonorCard(donor: DonorInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(donor.name.first().toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(donor.name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(donor.bloodGroup, color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Text("${donor.distance} away", fontSize = 12.sp, color = Color.Gray)
                Text("AI Match: ${donor.matchScore}%", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }

            // Actions
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /* Call */ }) {
                    Icon(Icons.Filled.Phone, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { /* Chat */ }) {
                    Icon(Icons.Filled.Email, contentDescription = "Chat", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

data class DonorInfo(
    val name: String,
    val bloodGroup: String,
    val distance: String,
    val status: String,
    val matchScore: Int
)
