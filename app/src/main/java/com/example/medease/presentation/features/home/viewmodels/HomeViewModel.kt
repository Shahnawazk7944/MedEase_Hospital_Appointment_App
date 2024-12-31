package com.example.medease.presentation.features.home.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medease.data.repository.auth.UserDataStoreRepository
import com.example.medease.data.repository.home.UserHomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userHomeRepository: UserHomeRepository,
    private val dataStoreRepository: UserDataStoreRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeStates())
    val homeState = _homeState.asStateFlow()

    init {
        homeEvents(HomeEvents.GetUserId)
    }

    fun homeEvents(event: HomeEvents) {
        when (event) {
            HomeEvents.OnLogoutClick -> {
                viewModelScope.launch {
                    logout()
                }
            }

            is HomeEvents.RemoveFailure -> {
                _homeState.update { it.copy(logoutFailure = event.failure) }
            }

            HomeEvents.GetUserId -> {
                viewModelScope.launch {
                    getUserId()
                }
            }

            is HomeEvents.GetUserProfile -> {
                viewModelScope.launch {
                    getUserProfile(event.userId)
                }
            }
        }
    }

    private suspend fun logout() {
        _homeState.update { it.copy(loggingOut = true) }
        userHomeRepository.logout().onRight { isSuccess ->
            delay(7000)
            _homeState.update {
                it.copy(
                    authenticated = isSuccess.authenticated,
                    loggingOut = false
                )
            }
        }.onLeft { failure ->
            _homeState.update {
                it.copy(
                    loggingOut = false,
                    logoutFailure = failure
                )
            }
        }
    }

    private suspend fun getUserId() {
        _homeState.update { it.copy(loading = true) }
        dataStoreRepository.getUserId().onRight { userId ->
            _homeState.update { it.copy(userId = userId) }
            homeEvents(HomeEvents.GetUserProfile(userId))
            _homeState.update { it.copy(loading = false) }
        }.onLeft { failure ->
            _homeState.update { it.copy(loading = false, userIdFailure = failure) }
            delay(4000)
            _homeState.update { it.copy(userIdFailure = null) }
            logout()
        }
    }

    private suspend fun getUserProfile(userId: String) {
        _homeState.update { it.copy(loading = true) }
        userHomeRepository.getUserProfile(userId).onRight { userProfile ->
            _homeState.update { it.copy(userProfile = userProfile, loading = false) }
        }.onLeft { failure ->
            Log.d("-------", "getUserProfile: $failure")
            _homeState.update { it.copy(loading = false, userProfileFailure = failure) }
            delay(4000)
            _homeState.update { it.copy(userProfileFailure = null) }
        }
    }
}
