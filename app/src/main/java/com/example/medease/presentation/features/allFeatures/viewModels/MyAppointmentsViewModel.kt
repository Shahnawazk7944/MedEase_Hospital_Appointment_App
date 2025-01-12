package com.example.medease.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medease.data.repository.allFeatures.UserAllFeaturesFailure
import com.example.medease.data.repository.allFeatures.UserAllFeaturesRepository
import com.example.medease.domain.model.AppointmentDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyAppointmentsStates(
    val loading: Boolean = false,
    val failure: UserAllFeaturesFailure? = null,
    val appointments: List<AppointmentDetails> = emptyList()
)

sealed class MyAppointmentsEvents {
    data class GetMyAppointments(val userId: String) : MyAppointmentsEvents()
    data object RemoveFailure : MyAppointmentsEvents()
}

@HiltViewModel
class MyAppointmentsViewModel @Inject constructor(
    private val repository: UserAllFeaturesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MyAppointmentsStates())
    val state = _state.asStateFlow()


    fun myAppointmentsEvents(event: MyAppointmentsEvents) {
        when (event) {
           is MyAppointmentsEvents.GetMyAppointments -> {
               fetchMyAppointments(event.userId)
           }

            MyAppointmentsEvents.RemoveFailure -> {_state.update { it.copy(failure = null) }}
        }
    }

    private fun fetchMyAppointments(userId: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchMyAppointments(userId = userId).onRight { myAppointments ->
                _state.update { it.copy(appointments = myAppointments, loading = false) }
            }.onLeft { failure ->
                _state.update {
                    it.copy(
                        loading = false,
                        failure = failure
                    )
                }
            }
        }
    }
}