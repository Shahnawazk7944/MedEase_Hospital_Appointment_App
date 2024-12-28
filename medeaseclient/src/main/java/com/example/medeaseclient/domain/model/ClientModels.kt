package com.example.medeaseclient.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientProfile(
    val hospitalId: String? = null,
    val hospitalName: String? = null,
    val hospitalEmail: String? = null,
    val hospitalPhone: String? = null,
    val hospitalCity: String? = null,
    val hospitalPinCode: String? = null,
)

@Serializable
data class Doctor(
    val doctorId: String = "",
    val hospitalId: String = "",
    val name: String = "",
    val specialist: String = "",
    val experience: String = "",
    val availabilityFrom: String = "",
    val availabilityTo: String = "",
    val generalAvailability: String = "",
    val currentAvailability: String = "",
    val emergencyAvailability: String = ""
)

@Serializable
data class Bed(
    val bedId: String = "",
    val bedType: String = "",
    val purpose: String = "",
    val features: List<String> = emptyList(),
    val perDayBedPriceINR: String = "",
    val availability: String = "",
    val availableUnits: String = "0"
)
