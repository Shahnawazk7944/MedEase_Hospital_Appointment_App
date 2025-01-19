package com.example.medeaseclient.presentation.features.home.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medeaseclient.data.repository.allFeatures.ClientAllFeaturesRepository
import com.example.medeaseclient.data.repository.auth.ClientDataStoreRepository
import com.example.medeaseclient.data.repository.home.ClientHomeRepository
import com.example.medeaseclient.data.util.Validator
import com.example.medeaseclient.presentation.features.home.viewmodels.events.AppointmentOperationEvents
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeEvents
import com.example.medeaseclient.presentation.features.home.viewmodels.events.HomeStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val clientAllFeaturesRepository: ClientAllFeaturesRepository,
    private val validator: Validator
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

            is HomeEvents.FetchAppointments -> {
                fetchAppointments(event.hospitalId)
            }
        }
    }

    fun appointmentOperationsEvents(event: AppointmentOperationEvents) {
        when (event) {
            is AppointmentOperationEvents.ChangeAppointmentStatus -> {
                changeAppointmentStatus(event.appointmentId, event.newStatus)
            }

            AppointmentOperationEvents.ClearAppointmentStatus -> {
                _homeState.update {
                    it.copy(
                        appointmentStatusSuccess = null,
                        appointmentStatusFailure = null
                    )
                }
            }

            is AppointmentOperationEvents.ChangeAddHealthRemark -> {
                val remarkError = validator.validateAddRemark(event.newRemark)
                _homeState.update {
                    it.copy(
                        addHealthRemark = event.newRemark,
                        addHealthRemarkError = remarkError?.message
                    )
                }

            }

            is AppointmentOperationEvents.ChangeNewAppointmentDate -> {
                val dateError =
                    validator.validateBookingDate(event.newDate, event.fromDate, event.toDate)
                _homeState.update {
                    it.copy(
                        newAppointmentDate = event.newDate,
                        newAppointmentDateError = dateError?.message
                    )
                }
            }

            is AppointmentOperationEvents.ChangeNewAppointmentTime -> {
                val timeError = validator.validateBookingTime(event.newTime)
                _homeState.update {
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

            AppointmentOperationEvents.ClearCompletedAppointment -> {
                _homeState.update {
                    it.copy(
                        appointmentStatusSuccess = null,
                        appointmentStatusFailure = null,
                        addHealthRemark = "",
                        addHealthRemarkError = null
                    )
                }
            }

            AppointmentOperationEvents.ClearReScheduledAppointment -> {
                _homeState.update {
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

    private suspend fun logout() {
        _homeState.update { it.copy(loggingOut = true) }
        clientHomeRepository.logout().onRight { isSuccess ->
            delay(1500)
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
            homeEvents(
                HomeEvents.FetchAppointments(
                    homeState.value.clientProfile?.hospitalId ?: "no hospital id found"
                )
            )
        }.onLeft { failure ->
            _homeState.update { it.copy(loading = false, clientProfileFailure = failure) }
            delay(4000)
            _homeState.update { it.copy(clientProfileFailure = null) }
        }
    }

    private fun fetchAppointments(hospitalId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            clientAllFeaturesRepository.fetchHospitalAppointments(hospitalId).collect {
                it.onRight { appointments ->
                    Log.d("----", "${appointments.size}")
                    _homeState.update { it.copy(todayAppointments = appointments) }
                }.onLeft { failure ->
                    _homeState.update { it.copy(appointmentsFailure = failure) }
                }
            }
        }
    }

    private fun changeAppointmentStatus(appointmentId: String, newStatus: String) {
        _homeState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientAllFeaturesRepository.changeAppointmentStatus(
                appointmentId = appointmentId,
                newStatus = newStatus
            )
                .onRight { success ->
                    _homeState.update {
                        it.copy(
                            appointmentStatusSuccess = success,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _homeState.update {
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
        _homeState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientAllFeaturesRepository.reScheduleAppointment(
                appointmentId = appointmentId,
                newDate = newDate,
                newTime = newTime,
                newStatus = newStatus
            )
                .onRight { success ->
                    _homeState.update {
                        it.copy(
                            appointmentStatusSuccess = success,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _homeState.update {
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
        _homeState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            clientAllFeaturesRepository.markCompletedAppointment(
                appointmentId = appointmentId,
                healthRemark = healthRemark,
                userId = userId,
                newStatus = newStatus
            )
                .onRight { success ->
                    _homeState.update {
                        it.copy(
                            appointmentStatusSuccess = success,
                            loading = false
                        )
                    }
                }.onLeft { failure ->
                    _homeState.update {
                        it.copy(
                            loading = false,
                            appointmentStatusFailure = failure
                        )
                    }
                }

        }
    }
}
