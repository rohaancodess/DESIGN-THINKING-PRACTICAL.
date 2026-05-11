package com.designthinkingpractical.app.domain.model

data class AuthResponse(
    val _id: String,
    val name: String,
    val email: String,
    val token: String
)

data class MessageResponse(
    val message: String
)
