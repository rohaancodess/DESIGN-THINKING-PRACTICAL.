package com.designthinkingpractical.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationScreen(
    viewModel: AuthViewModel,
    email: String,
    onVerified: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    var otpValues by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    
    var timeLeft by remember { mutableStateOf(300) } // 5 minutes

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.OtpVerified) {
            val data = viewModel.pendingRegistrationData
            if (data != null) {
                viewModel.register(
                    name = data["name"]!!,
                    email = data["email"]!!,
                    password = data["password"]!!,
                    bloodGroup = data["bloodGroup"]!!,
                    phone = data["phone"]!!,
                    address = data["address"]!!,
                    lastDonationDate = data["lastDonationDate"]!!
                )
                viewModel.pendingRegistrationData = null
            } else {
                onVerified()
                viewModel.resetState()
            }
        } else if (authState is AuthState.Success) {
            onVerified()
            viewModel.resetState()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Verify Email",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Code sent to $email",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 32.dp, top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (i in 0 until 6) {
                        OutlinedTextField(
                            value = otpValues[i],
                            onValueChange = { newValue ->
                                if (newValue.length <= 1) {
                                    val newValues = otpValues.toMutableList()
                                    newValues[i] = newValue
                                    otpValues = newValues
                                    if (newValue.isNotEmpty() && i < 5) {
                                        focusRequesters[i + 1].requestFocus()
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .focusRequester(focusRequesters[i]),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 20.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                if (authState is AuthState.Error) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        val otpString = otpValues.joinToString("")
                        if (otpString.length == 6) {
                            viewModel.verifyOtp(email, otpString)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Verify", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                val minutes = timeLeft / 60
                val seconds = timeLeft % 60
                Text(
                    text = "Resend OTP in ${String.format("%02d:%02d", minutes, seconds)}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                
                if (timeLeft == 0) {
                    TextButton(onClick = {
                        viewModel.sendOtp(email)
                        timeLeft = 300
                    }) {
                        Text("Resend OTP", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
