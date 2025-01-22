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
    val careAvailability: String = "",
    val emergencyAvailability: String = "",
    val treatedSymptoms: String = "",
    val generalFees: String = "",
    val careFees: String = "",
    val emergencyFees: String = "",
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

@Serializable
data class AppointmentDetails(
    val appointmentId: String = "",
    val hospitalId: String = "",
    val userId: String = "",
    val hospital: HospitalWithDoctors = HospitalWithDoctors(),
    val doctor: Doctor = Doctor(),
    val bed: Bed? = null, // Optional
    val bookingDate: String = "",
    val bookingTime: String = "",
    val bookingQuota: String = "",
    val totalPrice: String = "",
    val status: String = "Booking Confirmed"
)

@Serializable
data class PaymentDetails(
    val appointmentId: String = "",
    val transactionId: String = "",
    val userId: String = "",
    val date: String = "",
    val paymentType: String = "",
    val amountPaid: String = "",
    val adminCharges: String = "",
    val status: String = "Success"
)

@Serializable
data class Booking(
    val appointmentDetails: AppointmentDetails = AppointmentDetails(),
    val paymentDetails: PaymentDetails = PaymentDetails()
)
@Serializable
data class HealthRecord(
    val healthRecordId: String = "",
    val appointmentId: String = "",
    val healthRemark: String = "",
)