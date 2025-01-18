package com.example.medeaseclient.presentation.features.home.viewmodels.events

import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesFailure
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
)
