package com.designthinkingpractical.app.domain.model

data class User(
    val _id: String,
    val name: String,
    val email: String,
    val bloodGroup: String?,
    val city: String?,
    val donationCount: Int?,
    val verified: Boolean?,
    val phone: String?,
    val address: String?,
    val lastDonationDate: String?
)

