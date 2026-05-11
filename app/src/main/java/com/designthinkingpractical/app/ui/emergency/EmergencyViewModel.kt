package com.designthinkingpractical.app.ui.emergency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designthinkingpractical.app.domain.model.EmergencyRequest
import com.designthinkingpractical.app.domain.repository.EmergencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EmergencyViewModel(
    private val repository: EmergencyRepository
) : ViewModel() {

    val activeRequests: StateFlow<List<EmergencyRequest>> = repository.getActiveRequests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _bloodBanks = MutableStateFlow<List<com.designthinkingpractical.app.domain.model.BloodBank>>(emptyList())

    val bloodBanks: StateFlow<List<com.designthinkingpractical.app.domain.model.BloodBank>> = _bloodBanks

    init {
        fetchBloodBanks()
    }

    fun fetchBloodBanks() {
        viewModelScope.launch {
            repository.getBloodBanks().onSuccess {
                _bloodBanks.value = it
            }
        }
    }


    fun createRequest(
        patientName: String,
        bloodGroup: String,
        hospital: String,
        unitsRequired: Int,
        emergencyLevel: String,
        contactNumber: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.createRequest(
                patientName, bloodGroup, hospital, unitsRequired, emergencyLevel, contactNumber
            )
            if (result.isSuccess) {
                onSuccess()
            }
        }
    }
}
