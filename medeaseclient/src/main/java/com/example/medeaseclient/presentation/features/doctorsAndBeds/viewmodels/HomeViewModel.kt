package com.example.medeaseclient.presentation.features.doctorsAndBeds.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medeaseclient.data.repository.auth.ClientDataStoreRepository
import com.example.medeaseclient.data.repository.home.ClientHomeRepository
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeEvents
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clientHomeRepository: ClientHomeRepository,
    private val dataStoreRepository: ClientDataStoreRepository,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeStates())
    val homeState = _homeState.asStateFlow()

    init {
        homeEvents(HomeEvents.GetClientId)
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

            HomeEvents.GetClientId -> {
                viewModelScope.launch {
                    getClientId()
                }
            }

            is HomeEvents.GetClientProfile -> {
                viewModelScope.launch {
                    getClientProfile(event.clientId)
                }
            }
        }
    }

    private suspend fun logout() {
        _homeState.update { it.copy(loggingOut = true) }
        clientHomeRepository.logout().onRight { isSuccess ->
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

    private suspend fun getClientId() {
        _homeState.update { it.copy(loading = true) }
        dataStoreRepository.getClientId().onRight { userId ->
            _homeState.update { it.copy(clientId = userId) }
            homeEvents(HomeEvents.GetClientProfile(userId))
            _homeState.update { it.copy(loading = false) }
        }.onLeft { failure ->
            _homeState.update { it.copy(loading = false, clientIdFailure = failure) }
            delay(4000)
            _homeState.update { it.copy(clientIdFailure = null) }
            logout()
        }
    }

    private suspend fun getClientProfile(clientId: String) {
        _homeState.update { it.copy(loading = true) }
        clientHomeRepository.getClientProfile(clientId).onRight { clientProfile ->
            _homeState.update { it.copy(clientProfile = clientProfile, loading = false) }
        }.onLeft { failure ->
            _homeState.update { it.copy(loading = false, clientProfileFailure = failure) }
            delay(4000)
            _homeState.update { it.copy(clientProfileFailure = null) }
        }
    }
}
