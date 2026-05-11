package com.designthinkingpractical.app.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(donorName: String, onNavigateBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    
    // Simulate Socket.IO connection
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        isConnected = true
    }
    
    // Placeholder messages
    val messages = remember { mutableStateListOf(
        Message("Hello, I saw your emergency request. I am O+ and available to donate.", true),
        Message("Oh thank God! Where are you located?", false),
        Message("I am about 5 mins away from the hospital. I am on my way.", true)
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(donorName)
                        Text(if (isConnected) "Online" else "Connecting...", fontSize = 12.sp, color = if (isConnected) Color.Green else Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (messageText.isNotEmpty()) {
                            messages.add(Message(messageText, false))
                            messageText = ""
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.size) { index ->
                val msg = messages[index]
                ChatBubble(msg)
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isMe = !message.isReceived
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isMe) MaterialTheme.colorScheme.primary else Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.text,
                color = if (isMe) Color.White else Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

data class Message(val text: String, val isReceived: Boolean)
