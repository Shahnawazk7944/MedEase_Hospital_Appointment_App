package com.example.medeaseclient.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medeaseclient.data.repository.home.ClientHomeRepository
import com.example.medeaseclient.data.repository.home.LogoutFailure
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
    private val clientHomeRepository: ClientHomeRepository,
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileStates())
    val profileState = _profileState.asStateFlow()

    fun logout() {
        _profileState.update { it.copy(loggingOut = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientHomeRepository.logout().onRight { isSuccess ->
                delay(1500)
                _profileState.update {
                    it.copy(
                        loggingOut = false,
                        authenticated = isSuccess.authenticated
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