package com.designthinkingpractical.app.domain.model

data class EmergencyRequest(
    val id: String,
    val patientName: String,
    val bloodGroup: String,
    val hospital: String,
    val unitsRequired: Int,
    val emergencyLevel: String,
    val contactNumber: String,
    val status: String,
    val createdAt: String
)
