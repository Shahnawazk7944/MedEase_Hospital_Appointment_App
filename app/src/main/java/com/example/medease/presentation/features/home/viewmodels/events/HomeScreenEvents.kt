package com.example.medease.presentation.features.home.viewmodels.events

import com.example.medease.data.repository.home.LogoutFailure


sealed class HomeEvents {
    object OnLogoutClick : HomeEvents()
    data class RemoveFailure(val failure: LogoutFailure?) : HomeEvents()
    data object GetUserId : HomeEvents()
    data class GetUserProfile(val userId: String) : HomeEvents()
    //data object GetUserProfileFailure : HomeEvents()
}