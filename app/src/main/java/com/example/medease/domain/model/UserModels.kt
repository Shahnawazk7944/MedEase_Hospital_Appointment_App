package com.example.medease.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
)

@Serializable
data class HospitalWithDoctors(
    val hospitalCity: String = "",
    val hospitalEmail: String = "",
    val hospitalId: String = "",
    val hospitalName: String = "",
    val hospitalPhone: String = "",
    val hospitalPinCode: String = "",
    val doctors: List<Doctor> = emptyList()
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
    val emergencyAvailability: String = "",
    val treatedSymptoms: String = "",
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