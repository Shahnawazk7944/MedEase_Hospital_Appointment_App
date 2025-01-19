package com.example.medeaseclient.presentation.features.allFeatures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesFailure
import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesRepository
import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesSuccess
import com.example.medeaseclient.data.util.Validator
import com.example.medeaseclient.domain.model.AppointmentDetails
import com.example.medeaseclient.presentation.features.home.viewmodels.events.AppointmentOperationEvents
import com.example.medeaseclient.presentation.features.home.viewmodels.events.AppointmentOperationsStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyAppointmentsStates(
    val loading: Boolean = false,
    val failure: ClientAllFeaturesFailure? = null,
    val appointments: List<AppointmentDetails> = emptyList(),

    val appointmentStatusFailure: ClientAllFeaturesFailure? = null,
    val appointmentStatusSuccess: ClientAllFeaturesSuccess? = null,

    override val addHealthRemark: String = "",
    override val addHealthRemarkError: String? = null,
    override val newAppointmentDate: String = "",
    override val newAppointmentDateError: String? = null,
    override val newAppointmentTime: String = "",
    override val newAppointmentTimeError: String? = null
) : AppointmentOperationsStates

sealed class MyAppointmentsEvents {
    data class GetMyAppointments(val hospitalId: String) : MyAppointmentsEvents()
    data object RemoveFailure : MyAppointmentsEvents()
}

@HiltViewModel
class MyAppointmentsViewModel @Inject constructor(
    private val repository: ClientAllFeaturesRepository,
    private val validator: Validator
) : ViewModel() {

    private val _state = MutableStateFlow(MyAppointmentsStates())
    val state = _state.asStateFlow()


    fun myAppointmentsEvents(event: MyAppointmentsEvents) {
        when (event) {
            is MyAppointmentsEvents.GetMyAppointments -> {
                fetchMyAppointments(event.hospitalId)
            }

            MyAppointmentsEvents.RemoveFailure -> {
                _state.update { it.copy(failure = null) }
            }
        }
    }

    fun appointmentOperationsEvents(event: AppointmentOperationEvents) {
        when (event) {

            is AppointmentOperationEvents.ChangeAppointmentStatus -> {
                changeAppointmentStatus(event.appointmentId, event.newStatus)
            }

            is AppointmentOperationEvents.ChangeAddHealthRemark -> {
                val remarkError = validator.validateAddRemark(event.newRemark)
                _state.update {
                    it.copy(
                        addHealthRemark = event.newRemark,
                        addHealthRemarkError = remarkError?.message
                    )
                }

            }

            is AppointmentOperationEvents.ChangeNewAppointmentDate -> {
                val dateError =
                    validator.validateBookingDate(event.newDate, event.fromDate, event.toDate)
                _state.update {
                    it.copy(
                        newAppointmentDate = event.newDate,
                        newAppointmentDateError = dateError?.message
                    )
                }
            }

            is AppointmentOperationEvents.ChangeNewAppointmentTime -> {
                val timeError = validator.validateBookingTime(event.newTime)
                _state.update {
                    it.copy(
                        newAppointmentTime = event.newTime,
                        newAppointmentTimeError = timeError?.message
                    )
                }
            }

            is AppointmentOperationEvents.CompleteAppointment -> {
                markCompleteAppointment(
                    appointmentId = event.appointmentId,
                    healthRemark = event.healthRemark,
                    userId = event.userId,
                    newStatus = event.newStatus
                )
            }

            is AppointmentOperationEvents.ReScheduleAppointment -> {
                reScheduleAppointment(
                    appointmentId = event.appointmentId,
                    newDate = event.newDate,
                    newTime = event.newTime,
                    newStatus = event.newStatus
                )
            }

            AppointmentOperationEvents.ClearAppointmentStatus -> {
                _state.update {
                    it.copy(
                        appointmentStatusSuccess = null,
                        appointmentStatusFailure = null
                    )
                }
            }

            AppointmentOperationEvents.ClearCompletedAppointment -> {
                _state.update {
                    it.copy(
                        appointmentStatusSuccess = null,
                        appointmentStatusFailure = null,
                        addHealthRemark = "",
                        addHealthRemarkError = null
                    )
                }
            }

            AppointmentOperationEvents.ClearReScheduledAppointment -> {
                _state.update {
                    it.copy(
                        appointmentStatusSuccess = null,
                        appointmentStatusFailure = null,
                        newAppointmentDate = "",
                        newAppointmentDateError = null,
                        newAppointmentTime = "",
                        newAppointmentTimeError = null
                    )
                }
            }
        }
    }

    private fun fetchMyAppointments(hospitalId: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchHospitalAppointments(hospitalId = hospitalId).collect { result ->
                result.onRight { myAppointments ->
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

    private fun changeAppointmentStatus(appointmentId: String, newStatus: String) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.changeAppointmentStatus(
                appointmentId = appointmentId,
                newStatus = newStatus
            )
                .onRight { success ->
                    _state.update {
                        it.copy(
                            appointmentStatusSuccess = success,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            appointmentStatusFailure = failure
                        )
                    }
                }

        }
    }

    private fun reScheduleAppointment(
        appointmentId: String,
        newDate: String,
        newTime: String,
        newStatus: String
    ) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.reScheduleAppointment(
                appointmentId = appointmentId,
                newDate = newDate,
                newTime = newTime,
                newStatus = newStatus
            )
                .onRight { success ->
                    _state.update {
                        it.copy(
                            appointmentStatusSuccess = success,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            appointmentStatusFailure = failure
                        )
                    }
                }

        }
    }

    private fun markCompleteAppointment(
        appointmentId: String,
        healthRemark: String,
        userId: String,
        newStatus: String
    ) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.markCompletedAppointment(
                appointmentId = appointmentId,
                healthRemark = healthRemark,
                userId = userId,
                newStatus = newStatus
            )
                .onRight { success ->
                    _state.update {
                        it.copy(
                            appointmentStatusSuccess = success,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            loading = false,
                            appointmentStatusFailure = failure
                        )
                    }
                }

        }
    }
}