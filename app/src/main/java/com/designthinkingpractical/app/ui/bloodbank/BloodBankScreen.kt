package com.designthinkingpractical.app.ui.bloodbank

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.designthinkingpractical.app.ui.emergency.EmergencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodBankScreen(
    viewModel: EmergencyViewModel,
    onNavigateBack: () -> Unit
) {
    val bloodBanks by viewModel.bloodBanks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blood Banks") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(bloodBanks.size) { index ->
                val bank = bloodBanks[index]
                BloodBankCard(
                    bank.name, 
                    bank.address, 
                    bank.inventory.joinToString { "${it.bloodGroup}: ${it.units} units" }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun BloodBankCard(name: String, distance: String, inventory: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(distance, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Inventory: $inventory", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* Contact logic */ }) {
                Text("Reserve Units")
            }
        }
    }
}
