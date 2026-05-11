package com.designthinkingpractical.app.domain.model

data class BloodBank(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val inventory: List<BloodInventory>
)

data class BloodInventory(
    val bloodGroup: String,
    val units: Int
)
