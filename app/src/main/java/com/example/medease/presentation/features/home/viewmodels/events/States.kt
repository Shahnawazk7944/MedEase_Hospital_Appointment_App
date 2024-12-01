package com.example.medease.presentation.features.home.viewmodels.events

import com.example.medease.data.repository.auth.GetRememberMeAndIDPreferencesFailure
import com.example.medease.data.repository.home.LogoutFailure
import com.example.medease.data.repository.home.UserProfileFailure
import com.example.medease.domain.model.UserProfile

data class HomeStates(
    val loading: Boolean = false,
    val loggingOut: Boolean = false,
    val logoutFailure: LogoutFailure? = null,
    val authenticated: Boolean = true,
    val userId: String? = null,
    val userIdFailure: GetRememberMeAndIDPreferencesFailure? = null,
    val userProfile: UserProfile? = null,
    val userProfileFailure: UserProfileFailure? = null
)