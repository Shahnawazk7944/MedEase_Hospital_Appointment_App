package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HealthRecordsStates(
    val loading: Boolean = false,
)

sealed class HealthRecordsEvents {
    data object GetHealthRecords : HealthRecordsEvents()
}

@HiltViewModel
class HealthRecordsViewModel @Inject constructor(
    // private val clientHomeRepository: ClientHomeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HealthRecordsStates())
    val state = _state.asStateFlow()


    fun healthRecordsEvents(event: HealthRecordsEvents) {
        when (event) {
            HealthRecordsEvents.GetHealthRecords -> {}
        }
    }
}