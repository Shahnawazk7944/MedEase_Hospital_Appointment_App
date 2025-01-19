package com.example.medeaseclient.presentation.features.home.viewmodels.events

import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesFailure
import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesSuccess
import com.example.medeaseclient.data.repository.auth.GetRememberMeAndIDPreferencesFailure
import com.example.medeaseclient.data.repository.home.ClientProfileFailure
import com.example.medeaseclient.data.repository.home.LogoutFailure
import com.example.medeaseclient.domain.model.AppointmentDetails
import com.example.medeaseclient.domain.model.ClientProfile

data class HomeStates(
    val loading: Boolean = false,
    val loggingOut: Boolean = false,
    val logoutFailure: LogoutFailure? = null,
    val authenticated: Boolean = true,
    val clientId: String? = null,
    val clientIdFailure: GetRememberMeAndIDPreferencesFailure? = null,
    val clientProfile: ClientProfile? = null,
    val clientProfileFailure: ClientProfileFailure? = null,
    val todayAppointments: List<AppointmentDetails> = emptyList<AppointmentDetails>(),
    val appointmentsFailure: ClientAllFeaturesFailure? = null,
    val appointmentStatusFailure: ClientAllFeaturesFailure? = null,
    val appointmentStatusSuccess: ClientAllFeaturesSuccess? = null,

    override val addHealthRemark: String = "",
    override val addHealthRemarkError: String? = null,
    override val newAppointmentDate: String = "",
    override val newAppointmentDateError: String? = null,
    override val newAppointmentTime: String = "",
    override val newAppointmentTimeError: String? = null
) : AppointmentOperationsStates


interface AppointmentOperationsStates {
    val addHealthRemark: String
    val addHealthRemarkError: String?
    val newAppointmentDate: String
    val newAppointmentDateError: String?
    val newAppointmentTime: String
    val newAppointmentTimeError: String?
}

