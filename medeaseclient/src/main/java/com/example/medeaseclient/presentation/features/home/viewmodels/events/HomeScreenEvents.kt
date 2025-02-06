package com.example.medeaseclient.presentation.features.home.viewmodels.events

import com.example.medeaseclient.data.repository.home.LogoutFailure
import com.example.medeaseclient.domain.model.AppointmentDetails
import com.example.medeaseclient.domain.model.Doctor

sealed class HomeEvents {
    object OnLogoutClick : HomeEvents()
    data class RemoveFailure(val failure: LogoutFailure?) : HomeEvents()
    data object GetClientId : HomeEvents()
    data class GetClientProfile(val clientId: String) : HomeEvents()
    data class FetchAppointments(val hospitalId: String) : HomeEvents()
}

sealed class AppointmentBottomSheetContent {
    data class ReScheduleAppointment(
        val appointmentDetails: AppointmentDetails,
        val appointmentId: String,
        val newStatus: String
    ) : AppointmentBottomSheetContent()

    data class CompleteAppointment(
        val appointmentId: String,
        val userId: String,
        val newStatus: String
    ) : AppointmentBottomSheetContent()
}

sealed class AppointmentOperationEvents {
    data class ChangeAppointmentStatus(val appointmentId: String, val newStatus: String) :
        AppointmentOperationEvents()
    data class FetchReScheduleAppointmentDoctor(val doctorId: String) :
        AppointmentOperationEvents()

    object ClearAppointmentStatus : AppointmentOperationEvents()
    data class ChangeAddHealthRemark(val newRemark: String) : AppointmentOperationEvents()
    data class ChangeNewAppointmentDate(
        val newDate: String,
        val fromDate: String,
        val toDate: String
    ) : AppointmentOperationEvents()

    data class ChangeNewAppointmentTime(val newTime: String) : AppointmentOperationEvents()
    data class CompleteAppointment(
        val appointmentId: String,
        val userId: String,
        val healthRemark: String,
        val newStatus: String
    ) : AppointmentOperationEvents()

    data class ReScheduleAppointment(
        val appointmentId: String,
        val newDate: String,
        val newTime: String,
        val newStatus: String,
        val newDoctor: Doctor
    ) : AppointmentOperationEvents()

    object ClearCompletedAppointment : AppointmentOperationEvents()
    object ClearReScheduledAppointment : AppointmentOperationEvents()
}