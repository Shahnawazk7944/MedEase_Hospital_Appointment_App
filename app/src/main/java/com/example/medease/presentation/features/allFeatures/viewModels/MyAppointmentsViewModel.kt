package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MyAppointmentsStates(
    val loading: Boolean = false,
)

sealed class MyAppointmentsEvents {
    data object GetMyAppointments : MyAppointmentsEvents()
}

@HiltViewModel
class MyAppointmentsViewModel @Inject constructor(
    // private val clientHomeRepository: ClientHomeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MyAppointmentsStates())
    val state = _state.asStateFlow()


    fun myAppointmentsEvents(event: MyAppointmentsEvents) {
        when (event) {
            MyAppointmentsEvents.GetMyAppointments -> {}
        }
    }
}