package com.example.medease.presentation.features.home.viewmodels

import com.example.medease.data.repository.auth.GetRememberMeAndIDPreferencesFailure
import com.example.medease.data.repository.home.LogoutFailure
import com.example.medease.data.repository.home.UserOperationsFailure
import com.example.medease.data.repository.home.UserProfileFailure
import com.example.medease.domain.model.Bed
import com.example.medease.domain.model.Doctor
import com.example.medease.domain.model.HospitalWithDoctors
import com.example.medease.domain.model.UserProfile

val sampleHospitals = listOf(
    HospitalWithDoctors(
        hospitalName = "ZER Care",
        hospitalCity = "Pune",
        hospitalPinCode = "541314",
        hospitalPhone = "7564827018",
        doctors = listOf(
            Doctor(
                doctorId = "1",
                name = "Dr. Amit Sharma",
                specialist = "Gynaecologist",
                treatedSymptoms = "Severe Headaches, Seizures",
                experience = "5",
                generalAvailability = "10",
                careAvailability = "42",
                emergencyAvailability = "20",
                availabilityFrom = "28-12-2023",
                availabilityTo = "29-12-2023"
            )
        )
    ),
    HospitalWithDoctors(
        hospitalName = "City Care",
        hospitalCity = "Mumbai",
        hospitalPinCode = "561308",
        hospitalPhone = "7564827018",
        doctors = listOf(
            Doctor(
                doctorId = "3",
                name = "Dr. Rohan Mehta",
                experience = "10",
                specialist = "Menu surgeon",
                availabilityFrom = "28-12-2024",
                availabilityTo = "29-12-2024",
                generalAvailability = "Mon-Fri",
                careAvailability = "Available",
                emergencyAvailability = "Yes",
                treatedSymptoms = " Nausea, Vomiting"
            )
        )
    ),
    HospitalWithDoctors(
        hospitalName = "PSR Care",
        hospitalCity = "Delhi",
        hospitalPinCode = "201304",
        hospitalPhone = "7564827018",
        doctors = listOf(
            Doctor(
                doctorId = "7",
                name = "Dr. Vikram Patel",
                specialist = "Cardiologist",
                treatedSymptoms = "Chest pain, shortness of breath",
                experience = "12",
                generalAvailability = "Mon-Sat",
                careAvailability = "Available",
                emergencyAvailability = "Yes",
                availabilityFrom = "01-01-2024",
                availabilityTo = "31-01-2024"
            )
        )
    ),
    HospitalWithDoctors(
        hospitalName = "Apollo Care",
        hospitalCity = "Gurgaon",
        hospitalPinCode = "568923",
        hospitalPhone = "7564827018",
        doctors = listOf(
            Doctor(
                doctorId = "9",
                name = "Dr. Rahul Chopra",
                experience = "10",
                specialist = "Neurologist surgeon",
                availabilityFrom = "28-12-2024",
                availabilityTo = "29-12-2024",
                generalAvailability = "Mon-Fri",
                careAvailability = "Available",
                emergencyAvailability = "Yes",
                treatedSymptoms = "pregnancy, fatigue"
            )
        )
    )
)

data class HomeStates(
    val loading: Boolean = false,
    val loggingOut: Boolean = false,
    val fetchingHospitalsWithDoctors: Boolean = false,
    val fetchingHospitalsBeds: Boolean = false,
    val logoutFailure: LogoutFailure? = null,
    val authenticated: Boolean = true,
    val userId: String? = null,
    val userIdFailure: GetRememberMeAndIDPreferencesFailure? = null,
    val userProfile: UserProfile? = null,
    val userProfileFailure: UserProfileFailure? = null,
    val userOperationsFailure: UserOperationsFailure? = null,

    val hospitalsWithDoctors: List<HospitalWithDoctors> = emptyList(),
    val searchQuery: String = "",

    val selectedHospitalWithDoctors: HospitalWithDoctors? = null,
    val selectedDoctor: Doctor? = null,
    val selectedHospitalBeds: List<Bed> = emptyList(),
    val selectedBed: Bed? = null,

    val bookingDate: String = "",
    val bookingTime: String = "",
    val selectedQuota: String = "general",
    val bookingDateError: String? = null,
    val bookingTimeError: String? = null
)

sealed class HomeEvents {
    object OnLogoutClick : HomeEvents()
    data class RemoveFailure(val failure: LogoutFailure?) : HomeEvents()
    data object GetUserId : HomeEvents()
    data class GetUserProfile(val userId: String) : HomeEvents()

    data class SearchQueryChange(val newQuery: String) : HomeEvents()
    object FetchHospitalsWithDoctors : HomeEvents()
    data class OnBookAppointmentClick(
        val hospitalWithDoctors: HospitalWithDoctors,
        val doctor: Doctor
    ) : HomeEvents()
    data class FetchHospitalBeds(val hospitalId: String) : HomeEvents()
    data class OnSelectBedClick(val bed: Bed?) : HomeEvents()
    data class BookAppointment(
        val hospitalWithDoctors: HospitalWithDoctors,
        val doctor: Doctor,
        val bed: Bed? = null
    ) : HomeEvents()

    data class BookingDateChange(val newDate: String, val fromDate: String, val toDate: String) : HomeEvents()
    data class BookingTimeChange(val newTime: String) : HomeEvents()
    data class BookingQuotaChange(val newQuota: String) : HomeEvents()

}