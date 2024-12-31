package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ProfileStates(
    val loading: Boolean = false,
)

sealed class ProfileEvents {
    data object GetProfile : ProfileEvents()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // private val clientHomeRepository: ClientHomeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileStates())
    val state = _state.asStateFlow()


    fun profileEvents(event: ProfileEvents) {
        when (event) {
            ProfileEvents.GetProfile -> {}
        }
    }
}