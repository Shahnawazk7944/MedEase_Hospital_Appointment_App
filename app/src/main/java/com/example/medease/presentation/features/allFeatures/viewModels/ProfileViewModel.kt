package com.example.medease.presentation.features.allFeatures.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medease.data.repository.home.LogoutFailure
import com.example.medease.data.repository.home.UserHomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileStates(
    val loggingOut: Boolean = false,
    val logoutFailure: LogoutFailure? = null,
    val authenticated: Boolean = true,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userHomeRepository: UserHomeRepository,
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileStates())
    val profileState = _profileState.asStateFlow()

    fun logout() {
        _profileState.update { it.copy(loggingOut = true) }
        Log.d("---- ProfileViewModel", "logging out")
        viewModelScope.launch(Dispatchers.IO) {
            userHomeRepository.logout().onRight { isSuccess ->
                delay(1500)
                Log.d("---- ProfileViewModel", "logout: $isSuccess")
                _profileState.update {
                    it.copy(
                        authenticated = isSuccess.authenticated,
                        loggingOut = false
                    )
                }
            }.onLeft { failure ->
                _profileState.update {
                    it.copy(
                        loggingOut = false,
                        logoutFailure = failure
                    )
                }
            }
        }
    }
}