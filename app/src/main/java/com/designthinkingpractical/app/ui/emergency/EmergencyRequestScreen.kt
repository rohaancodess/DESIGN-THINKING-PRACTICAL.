package com.designthinkingpractical.app.ui.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyRequestScreen(
    viewModel: EmergencyViewModel,
    onNavigateBack: () -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var hospital by remember { mutableStateOf("") }
    var units by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var emergencyLevel by remember { mutableStateOf("Urgent") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Blood") },
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
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Emergency Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = patientName, onValueChange = { patientName = it },
                label = { Text("Patient Name") }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = bloodGroup, onValueChange = { bloodGroup = it },
                label = { Text("Blood Group (e.g., O+)") }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = hospital, onValueChange = { hospital = it },
                label = { Text("Hospital Name & Location") }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = units, onValueChange = { units = it },
                    label = { Text("Units") }, modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp), singleLine = true
                )
                OutlinedTextField(
                    value = contact, onValueChange = { contact = it },
                    label = { Text("Contact No.") }, modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp), singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text("Emergency Level", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LevelButton("Normal", emergencyLevel == "Normal") { emergencyLevel = "Normal" }
                LevelButton("Urgent", emergencyLevel == "Urgent") { emergencyLevel = "Urgent" }
                LevelButton("Critical", emergencyLevel == "Critical") { emergencyLevel = "Critical" }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.createRequest(
                        patientName = patientName,
                        bloodGroup = bloodGroup,
                        hospital = hospital,
                        unitsRequired = units.toIntOrNull() ?: 1,
                        emergencyLevel = emergencyLevel,
                        contactNumber = contact,
                        onSuccess = onNavigateBack
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Broadcast Emergency Request", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}


@Composable
fun RowScope.LevelButton(level: String, isSelected: Boolean, onClick: () -> Unit) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(level, fontSize = 12.sp)
    }
}
