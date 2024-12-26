package com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels.events

import com.example.medeaseclient.data.repository.home.LogoutFailure


sealed class HomeEvents {
    object OnLogoutClick : HomeEvents()
    data class RemoveFailure(val failure: LogoutFailure?) : HomeEvents()
    data object GetClientId : HomeEvents()
    data class GetClientProfile(val clientId: String) : HomeEvents()
}